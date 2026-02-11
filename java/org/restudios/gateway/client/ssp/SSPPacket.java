package org.restudios.gateway.client.ssp;

import lombok.AllArgsConstructor;
import org.apache.commons.imaging.common.PackBits;
import org.restudios.gateway.utils.ZipUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Speeky * swimery
 * org.restudios.gateway.ssp.connection.packets
 *
 * @author ReStudios 26 07 2025
 */
public class SSPPacket {
    /**
     * id
     */
    private Long i;
    /**
     * reply. incoming packet id. nullable
     */
    private Long r;
    /**
     * zip format
     */
    private SSPZipFormat z;
    /**
     * data
     */
    private byte[] d;

    public SSPPacket(Long id, Long reply, byte[] d) {
        this.i = id;
        this.r = reply;
        this.d = d;
        this.z = calculateZipFormat();
    }

    public SSPPacket(Long id, Long reply, SSPZipFormat z, byte[] d) {
        this.i = id;
        this.r = reply;
        this.z = z;
        this.d = d;
    }

    public byte[] getData() {
        return d;
    }

    private SSPZipFormat calculateZipFormat() {
        if (d.length < 25) {
            return SSPZipFormat.PLAIN;
        } else if (d.length < 100) {
            return SSPZipFormat.PACKBITS;
        } else {
            return SSPZipFormat.GZIP;
        }
    }

    public void write(SSP ssp, OutputStream outputStream) throws Exception {
        if (outputStream == null) {
            throw new IllegalArgumentException("OutputStream cannot be null");
        }
        if (d == null) {
            throw new IllegalStateException("Data array cannot be null");
        }
        if (z == null) {
            throw new IllegalStateException("Zip format cannot be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dataOut = new DataOutputStream(baos)) {
            dataOut.writeLong(i); // 8 bytes
            dataOut.writeByte(r == null ? 0 : 1); // 1 byte
            if (r != null) {
                dataOut.writeLong(r); // 8 bytes
            }
            dataOut.writeByte(z.ordinal()); // 1 byte

            byte[] finalData = prepareData();
            dataOut.writeInt(finalData.length); // 4 bytes
            dataOut.write(finalData); // n bytes
        }
        try (DataOutputStream dataOut = new DataOutputStream(outputStream)) {
            byte[] encr = ssp.encrypt(baos.toByteArray());
            dataOut.writeInt(encr.length);
            dataOut.write(encr);
        }
    }

    private byte[] prepareData() throws IOException {
        switch (this.z) {
            case PLAIN:
                return this.d;

            case GZIP:
                return ZipUtils.gzipCompress(d);

            default: // PackBits или другой формат
                byte[] compressedData = PackBits.compress(this.d);
                int originalLength = d.length;
                int compressedLength = compressedData.length;

                ByteBuffer buffer = ByteBuffer.allocate(8 + compressedLength); // 4+4+data
                buffer.putInt(originalLength);
                buffer.putInt(compressedLength);
                buffer.put(compressedData);
                return buffer.array();
        }
    }

    public static SSPPacket read(SSP ssp, InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }

        ByteArrayInputStream dataInput;
        try (DataInputStream dataIn = new DataInputStream(inputStream)) {
            int i = dataIn.readInt();
            byte[] data = new byte[i];
            dataIn.readFully(data);
            dataInput = new ByteArrayInputStream(ssp.decrypt(data));
        }
        return fromPacketBytes(dataInput);
    }

    public static SSPPacket read(SSP ssp, byte[] bytes) throws Exception {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }

        ByteArrayInputStream dataInput;
        try (DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(bytes))) {
            int i = dataIn.readInt();
            byte[] data = new byte[i];
            dataIn.readFully(data);
            dataInput = new ByteArrayInputStream(ssp.decrypt(data));
        }
        return fromPacketBytes(dataInput);
    }

    public static SSPPacket fromPacketBytes(InputStream stream) throws IOException {
        try (DataInputStream dataIn = new DataInputStream(stream)) {
            long i = dataIn.readLong();

            byte hasR = dataIn.readByte();
            Long r = (hasR == 1) ? dataIn.readLong() : null;

            byte zipFormatOrdinal = dataIn.readByte();
            if (zipFormatOrdinal < 0 || zipFormatOrdinal >= SSPZipFormat.values().length) {
                throw new IOException("Invalid zip format ordinal: " + zipFormatOrdinal);
            }

            int dataLength = dataIn.readInt();
            if (dataLength < 0) {
                throw new IOException("Invalid data length: " + dataLength);
            }

            byte[] compressedData = new byte[dataLength];
            dataIn.readFully(compressedData); // более надежно чем readNBytes

            SSPZipFormat zipFormat = SSPZipFormat.values()[zipFormatOrdinal];
            byte[] decompressedData = decompressData(compressedData, zipFormat);

            return new SSPPacket(i, r, zipFormat, decompressedData);
        }
    }

    private static byte[] decompressData(byte[] compressedData, SSPZipFormat zipFormat) throws IOException {
        switch (zipFormat) {
            case PLAIN:
                return compressedData;

            case GZIP:
                return ZipUtils.gzipDecompress(compressedData);

            default: // PackBits или другой формат
                if (compressedData.length < 8) {
                    throw new IOException("Insufficient data for PackBits format");
                }

                ByteBuffer buffer = ByteBuffer.wrap(compressedData);
                int originalLength = buffer.getInt();
                int packBitsLength = buffer.getInt();

                if (originalLength < 0 || packBitsLength < 0) {
                    throw new IOException("Invalid lengths in PackBits data");
                }
                if (packBitsLength != compressedData.length - 8) {
                    throw new IOException("PackBits length mismatch");
                }

                byte[] packBitsData = new byte[packBitsLength];
                buffer.get(packBitsData);

                return PackBits.decompress(packBitsData, originalLength);
        }
    }

    public Long getId() {
        return this.i;
    }
    public Long getReply() {
        return this.r;
    }
}
