package org.restudios.gateway.client.ssp;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectStringify {
    public static String stringify(SspClasses.GeneralClasses.SlClass obj) {
        Class<? extends SspClasses.GeneralClasses.SlClass> clazz = obj.getClass();
        Map<String, String> fields = new HashMap<>();
        for (Field field : clazz.getFields()) {
            try {
                Object value = field.get(obj);
                if (value == null) {
                    fields.put(field.getName(), null);
                } else {
                    if (value instanceof CharSequence) {
                        fields.put(field.getName(), "\""+value+"\"");
                    } else {
                        fields.put(field.getName(), value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
            }
        }
        String clazzName = clazz.getName();
        if (clazzName.contains("SspClasses.")) clazzName = clazzName.substring(clazzName.lastIndexOf("SspClasses.") + 1);

        String result = clazzName;
        if (!fields.isEmpty()) {
            result += " { ";
            result += fields.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(", "));
            result += " }";
        }
        return result;
    }
}
