package com.github.davidnery.rydeminecraftprotocol.utils;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtils {

    public static byte[] compress(byte[] uncompressedData) {
        Deflater compressor = new Deflater();
        compressor.setInput(uncompressedData);
        compressor.finish();

        byte[] output = new byte[uncompressedData.length];
        compressor.deflate(output);

        compressor.end();

        return output;
    }

    public static byte[] decompress(byte[] compressedData, int originalSize) throws DataFormatException {
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData, 0, compressedData.length);
        byte[] result = new byte[originalSize];
        decompressor.inflate(result);
        decompressor.end();

        return result;
    }

}
