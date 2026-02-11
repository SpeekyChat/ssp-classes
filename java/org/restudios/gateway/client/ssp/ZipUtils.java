package org.restudios.gateway.client.ssp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Speeky * swimery
 * org.restudios.gateway.utils
 *
 * @author ReStudios 26 07 2025
 */
public class ZipUtils {
    public static byte[] gzipCompress(byte[] data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data);
        }
        return byteStream.toByteArray();
    }
    public static byte[] gzipDecompress(byte[] data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(data))) {
            byteStream.write(gzipStream.readAllBytes());
        }
        return byteStream.toByteArray();
    }
}
