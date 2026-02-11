package org.restudios.gateway.client.ssp;

import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ObjectSerializer {
    private enum ValueType {
        OBJECT, ENUM, INT8, INT16, INT32, INT64, BOOL, ARRAY, LIST, STRING, DATETIME, NULL,
        BLOB, INT8ARRAY, FLOAT32, FLOAT64
    }

    public Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            ObjectSerializer.ValueType type = ValueType.values()[in.readByte()];
            int len = in.readInt();
            byte[] child = in.readNBytes(len);

            switch (type) {
                case NULL:
                    return null;
                case STRING:
                    return new String(child, StandardCharsets.UTF_8);
                case BOOL:
                    return child[0] == 1;
                case INT64:
                    return ByteBuffer.wrap(child).getLong();
                case FLOAT32:
                    return ByteBuffer.wrap(child).getFloat();
                case FLOAT64:
                    return ByteBuffer.wrap(child).getDouble();
                case INT32:
                    return ByteBuffer.wrap(child).getInt();
                case INT16:
                    return ByteBuffer.wrap(child).getShort();
                case INT8:
                    return child[0];
                case DATETIME:
                    return LocalDateTime.parse(new String(child, StandardCharsets.UTF_8),
                            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")).toInstant(ZoneOffset.UTC);
                case ENUM:
                    return deserializeEnum(child);
                case OBJECT:
                    return deserializeObject(child);
                case ARRAY:
                    return deserializeArray(child);
                case INT8ARRAY:
                    return deserialize8Array(child);
                case LIST:
                    return deserializeList(child);
                case BLOB:
                    return deserializeBlob(child);
                default:
                    throw new RuntimeException("Unknown type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize", e);
        }
    }

    private Object deserializeBlob(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            long length = in.readLong();

            UUID uuid = UUID.randomUUID();

            return new SspClasses.TransferredFile(length, in.readNBytes((int)length));
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize array", e);
        }
    }

    private Object deserializeArray(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            ValueType elementType = ValueType.values()[in.readByte()];

            int length = in.readInt();

            List<Object> elements = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                int elemLength = in.readInt();
                byte[] elemData = in.readNBytes(elemLength);
                elements.add(deserialize(elemData));
            }

            if (length > 0 && elements.get(0) != null) {
                Class<?> componentType = elements.get(0).getClass();
                boolean allSameType = elements.stream()
                        .filter(Objects::nonNull)
                        .allMatch(e -> componentType.isAssignableFrom(e.getClass()));

                if (allSameType) {
                    Object array = Array.newInstance(componentType, length);
                    for (int i = 0; i < length; i++) {
                        Array.set(array, i, elements.get(i));
                    }
                    return array;
                }
            }

            return elements.toArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize array", e);
        }
    }


    private Object deserialize8Array(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            int length = in.readInt();

            byte[] elements = new byte[length];
            for (int i = 0; i < length; i++) {
                elements[i] = in.readByte();
            }
            return elements;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize array", e);
        }
    }

    private List<Object> deserializeList(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            ValueType elementType = ValueType.values()[in.readByte()];

            int size = in.readInt();

            List<Object> list = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                int elemLength = in.readInt();
                byte[] elemData = in.readNBytes(elemLength);
                list.add(deserialize(elemData));
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize list", e);
        }
    }

    private Object deserializeObject(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            int typeNameLength = in.readInt();
            String typeName = new String(in.readNBytes(typeNameLength), StandardCharsets.UTF_8);

            int fieldCount = in.readInt();

            Map<String, Object> fieldsMap = new LinkedHashMap<>();
            for (int i = 0; i < fieldCount; i++) {
                int fieldNameLength = in.readInt();
                String fieldName = new String(in.readNBytes(fieldNameLength), StandardCharsets.UTF_8);
                int fieldValueLength = in.readInt();
                byte[] fieldValue = in.readNBytes(fieldValueLength);
                Object value = deserialize(fieldValue);

                fieldsMap.put(fieldName, value);
            }

            try {
                Class<?> clazz = Class.forName(SspClasses.class.getPackageName() + ".SspClasses$" + typeName.replace(".", "$"));
                Objenesis obj = new SpringObjenesis();
                ObjectInstantiator<?> objectInstantiator = obj.getInstantiatorOf(clazz);
                Object instance = objectInstantiator.newInstance();

                for (Field field : getAllFields(clazz, instance)) {
                    if (fieldsMap.containsKey(field.getName())) {
                        Object value = fieldsMap.get(field.getName());
                        if (value instanceof Number num) {
                            field.set(instance, reCast(field.getType(), num));
                        } else {
                            field.set(instance, value);
                        }
                    }
                }
                
                return instance;

            } catch (ClassNotFoundException e) {
                Map<String, Object> objectMap = new LinkedHashMap<>();
                objectMap.put("__type", typeName);
                objectMap.putAll(fieldsMap);
                return objectMap;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize object", e);
        }
    }public Object reCast(Class<?> required, Number num) {
        if (Byte.class.isAssignableFrom(required) || byte.class == required) {
            return num.byteValue();
        }
        if (Short.class.isAssignableFrom(required) || short.class == required) {
            return num.shortValue();
        }
        if (Integer.class.isAssignableFrom(required) || int.class == required) {
            return num.intValue();
        }
        if (Long.class.isAssignableFrom(required) || long.class == required) {
            return num.longValue();
        }
        return num;
    }

    private Object deserializeEnum(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try (DataInputStream in = new DataInputStream(bais)) {
            int typeNameLength = in.readInt();
            String typeName = new String(in.readNBytes(typeNameLength), StandardCharsets.UTF_8);
            int valueLength = in.readInt();
            String value = new String(in.readNBytes(valueLength), StandardCharsets.UTF_8);

            try {
                Class<?> enumClass = Class.forName(SspClasses.class.getPackageName() + ".SspClasses$" + typeName.replace(".", "$"));
                for (Object enumConstant : enumClass.getEnumConstants()) {
                    if (((Enum<?>) enumConstant).name().equals(value)) {
                        return enumConstant;
                    }
                }
                throw new RuntimeException("Enum value " + value + " not found in " + typeName);
            } catch (ClassNotFoundException e) {
                Map<String, Object> enumMap = new HashMap<>();
                enumMap.put("__type", "enum");
                enumMap.put("__enumType", typeName);
                enumMap.put("value", value);
                return enumMap;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize enum", e);
        }
    }

    public byte[] serialize(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            byte type;
            byte[] data;

            if (obj == null) {
                type = (byte) ValueType.NULL.ordinal();
                data = new byte[0];
            } else if (obj instanceof SspClasses.GeneralClasses.SlClass) {
                type = (byte) ValueType.OBJECT.ordinal();
                data = serializeObject(obj);
            } else if (obj instanceof Enum<?> en) {
                type = (byte) ValueType.ENUM.ordinal();
                data = serializeEnum(en);
            } else if (obj instanceof Boolean b) {
                type = (byte) ValueType.BOOL.ordinal();
                data = new byte[]{(byte) (b == Boolean.TRUE ? 1 : 0)};
            } else if (obj instanceof Byte) {
                type = (byte) ValueType.INT8.ordinal();
                data = new byte[]{((Number) obj).byteValue()};
            } else if (obj instanceof Short) {
                type = (byte) ValueType.INT16.ordinal();
                data = ByteBuffer.allocate(2).putShort((Short) obj).array();
            } else if (obj instanceof Integer) {
                type = (byte) ValueType.INT32.ordinal();
                data = ByteBuffer.allocate(4).putInt((Integer) obj).array();
            } else if (obj instanceof Float) {
                type = (byte) ValueType.FLOAT32.ordinal();
                data = ByteBuffer.allocate(4).putFloat((Float) obj).array();
            } else if (obj instanceof Double) {
                type = (byte) ValueType.FLOAT64.ordinal();
                data = ByteBuffer.allocate(8).putDouble((Double) obj).array();
            } else if (obj instanceof Long) {
                type = (byte) ValueType.INT64.ordinal();
                data = ByteBuffer.allocate(8).putLong((Long) obj).array();
            } else if (obj instanceof String) {
                type = (byte) ValueType.STRING.ordinal();
                data = ((String) obj).getBytes(StandardCharsets.UTF_8);
            } else if (obj instanceof Instant instant) {
                type = (byte) ValueType.DATETIME.ordinal();
                data = (DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                        .withZone(ZoneId.of("UTC"))
                        .format(instant)).getBytes(StandardCharsets.UTF_8);
            } else if (obj instanceof List<?> list) {
                type = (byte) ValueType.LIST.ordinal();
                data = serializeList(list);
            } else if (obj instanceof byte[] byteArray) {
                type = (byte) ValueType.INT8ARRAY.ordinal();
                data = serialize8List(byteArray);
            } else if (obj instanceof Map<?, ?> map && map.containsKey("__type")) {
                // Десериализованный объект или enum
                String objectType = (String) map.get("__type");
                if ("enum".equals(objectType)) {
                    type = (byte) ValueType.ENUM.ordinal();
                    data = serializeEnumFromMap(map);
                } else {
                    type = (byte) ValueType.OBJECT.ordinal();
                    data = serializeObjectFromMap(map);
                }
            } else if (obj.getClass().isArray()) {
                type = (byte) ValueType.ARRAY.ordinal();
                data = serializeArray(obj);
            } else if (obj instanceof SspClasses.TransferredFile transferredFile) {
                type = (byte) ValueType.BLOB.ordinal();
                data = serializeFile(transferredFile);
            } else {
                // Любой другой объект сериализуем как универсальный объект
                type = (byte) ValueType.OBJECT.ordinal();
                data = serializeObject(obj);
            }

            out.writeByte(type);         // 1  byte  - type
            out.writeInt(data.length);   // 4  bytes - length
            out.write(data);             // n  bytes - data

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object", e);
        }

        return baos.toByteArray();
    }

    private byte[] serializeFile(SspClasses.TransferredFile file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(baos)) {
            out.writeLong(file.length);
            out.write(file.data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize blob", e);
        }

        return baos.toByteArray();
    }

    private byte[] serializeArray(Object array) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            int length = Array.getLength(array);

            // Определяем тип элементов из первого непустого элемента
            ValueType elementType = ValueType.NULL;
            for (int i = 0; i < length; i++) {
                Object element = Array.get(array, i);
                if (element != null) {
                    elementType = getValueType(element);
                    break;
                }
            }

            // Записываем тип элементов
            out.writeByte((byte) elementType.ordinal());

            // Записываем количество элементов
            out.writeInt(length);

            // Сериализуем каждый элемент
            for (int i = 0; i < length; i++) {
                Object element = Array.get(array, i);
                byte[] elementData = serialize(element);
                out.writeInt(elementData.length);
                out.write(elementData);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize array", e);
        }

        return baos.toByteArray();
    }

    private byte[] serializeList(List<?> list) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            // Определяем тип элементов из первого непустого элемента
            ValueType elementType = ValueType.NULL;
            for (Object item : list) {
                if (item != null) {
                    elementType = getValueType(item);
                    break;
                }
            }

            // Записываем тип элементов
            out.writeByte((byte) elementType.ordinal());

            // Записываем количество элементов
            out.writeInt(list.size());

            // Сериализуем каждый элемент
            for (Object element : list) {
                byte[] elementData = serialize(element);
                out.writeInt(elementData.length);
                out.write(elementData);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize list", e);
        }

        return baos.toByteArray();
    }

    private byte[] serialize8List(byte[] list) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            out.writeInt(list.length);
            out.write(list);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize int8 list", e);
        }

        return baos.toByteArray();
    }

    private byte[] serializeEnum(Enum<?> object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            // Используем простое имя enum'а без пакета
            String enumTypeName = object.getClass().getCanonicalName().substring((SspClasses.class.getPackageName() + ".SspClasses$").length());
            byte[] typeNameBytes = enumTypeName.getBytes(StandardCharsets.UTF_8);
            out.writeInt(typeNameBytes.length);
            out.write(typeNameBytes);

            String enumValue = object.name();
            byte[] enumValueBytes = enumValue.getBytes(StandardCharsets.UTF_8);
            out.writeInt(enumValueBytes.length);
            out.write(enumValueBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize enum", e);
        }
        return baos.toByteArray();
    }

    private byte[] serializeEnumFromMap(Map<?, ?> enumMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            String enumTypeName = (String) enumMap.get("__enumType");
            byte[] typeNameBytes = enumTypeName.getBytes(StandardCharsets.UTF_8);
            out.writeInt(typeNameBytes.length);
            out.write(typeNameBytes);

            String enumValue = (String) enumMap.get("value");
            byte[] enumValueBytes = enumValue.getBytes(StandardCharsets.UTF_8);
            out.writeInt(enumValueBytes.length);
            out.write(enumValueBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize enum from map", e);
        }
        return baos.toByteArray();
    }

    private byte[] serializeObject(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            String className = object.getClass().getCanonicalName().substring((SspClasses.class.getPackageName() + ".SspClasses$").length());
            byte[] nameBytes = className.getBytes(StandardCharsets.UTF_8);
            out.writeInt(nameBytes.length);
            out.write(nameBytes);

            List<Field> fields = getAllFields(object.getClass(), object);
            int fieldCount = 0;

            ByteArrayOutputStream temp = new ByteArrayOutputStream();
            try (DataOutputStream tempOut = new DataOutputStream(temp)) {
                for (Field field : fields) {
                    Object value = field.get(object);
                    byte[] fieldNameBytes = field.getName().getBytes(StandardCharsets.UTF_8);
                    byte[] fieldData = serialize(value);

                    tempOut.writeInt(fieldNameBytes.length);
                    tempOut.write(fieldNameBytes);
                    tempOut.writeInt(fieldData.length);
                    tempOut.write(fieldData);
                    fieldCount++;
                }
            }

            out.writeInt(fieldCount);
            out.write(temp.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object "+object.getClass().getName(), e);
        }
        return baos.toByteArray();
    }

    private byte[] serializeObjectFromMap(Map<?, ?> objectMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataOutputStream out = new DataOutputStream(baos)) {
            String typeName = (String) objectMap.get("__type");
            byte[] nameBytes = typeName.getBytes(StandardCharsets.UTF_8);
            out.writeInt(nameBytes.length);
            out.write(nameBytes);

            int fieldCount = objectMap.size() - 1; // -1 для __type
            out.writeInt(fieldCount);

            for (Map.Entry<?, ?> entry : objectMap.entrySet()) {
                String key = (String) entry.getKey();
                if ("__type".equals(key)) continue;

                byte[] fieldNameBytes = key.getBytes(StandardCharsets.UTF_8);
                out.writeInt(fieldNameBytes.length);
                out.write(fieldNameBytes);

                byte[] fieldData = serialize(entry.getValue());
                out.writeInt(fieldData.length);
                out.write(fieldData);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object from map", e);
        }
        return baos.toByteArray();
    }

    // Вспомогательные методы
    private ValueType getValueType(Object obj) {
        if (obj == null) return ValueType.NULL;
        if (obj instanceof Boolean) return ValueType.BOOL;
        if (obj instanceof Byte) return ValueType.INT8;
        if (obj instanceof Short) return ValueType.INT16;
        if (obj instanceof Integer) return ValueType.INT32;
        if (obj instanceof Long) return ValueType.INT64;
        if (obj instanceof String) return ValueType.STRING;
        if (obj instanceof Instant) return ValueType.DATETIME;
        if (obj instanceof Enum<?>) return ValueType.ENUM;
        if (obj instanceof List<?>) return ValueType.LIST;
        if (obj.getClass().isArray()) return ValueType.ARRAY;
        return ValueType.OBJECT;
    }

    // Остальные вспомогательные методы остаются без изменений
    private List<Field> getAllFields(Class<?> c, Object o) {
        List<Field> inc = new ArrayList<>();
        for (Field declaredField : c.getDeclaredFields()) {
            if (Modifier.isTransient(declaredField.getModifiers())) continue;
            if (Modifier.isStatic(declaredField.getModifiers())) continue;
            if (!declaredField.canAccess(o)) {
                declaredField.setAccessible(true);
            }
            inc.add(declaredField);
        }
        if (c.getSuperclass() != null && c.getSuperclass() != Object.class) {
            inc.addAll(getAllFields(c.getSuperclass(), o));
        }
        return inc;
    }

    public Field getField(Class<?> o, String name) {
        try {
            return o.getDeclaredField(name);
        } catch (NoSuchFieldException ignored) {
        }
        if (o.getSuperclass() != null && o.getSuperclass() != Object.class) {
            return getField(o.getSuperclass(), name);
        }
        return null;
    }

    private Object getOfClass(Object[] datas, Class<?> clazz) {
        if (datas == null) return null;
        for (Object object : datas) {
            if (clazz.isAssignableFrom(object.getClass())) {
                return object;
            }
        }
        return null;
    }
}
