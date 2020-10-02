package com.github.davidnery.rydeminecraftprotocol.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class ByteUtils {

    private int lastReadedBytes;

    public ByteUtils() {
        lastReadedBytes = 0;
    }

    public synchronized void writeString(DataOutputStream out, String string, Charset charset) throws IOException {
        byte[] bytes = string.getBytes(charset);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    public synchronized String readString(DataInputStream in, Charset charset) throws IOException {
        lastReadedBytes = 0;
        int length = readVarInt(in);
        byte[] data = new byte[length];
        in.readFully(data);
        lastReadedBytes = data.length;

        return new String(data, charset);
    }

    public synchronized int readVarInt(DataInputStream input) throws IOException {
        lastReadedBytes = 0;

        int out = 0;
        byte in;
        do {
            in = input.readByte();
            out |= (in & 0x7F) << lastReadedBytes++ * 7;
            if (lastReadedBytes > 5) throw new IOException("VarInt too big!");
        } while ((in & 0x80) == 0x80);

        return out;
    }

    public synchronized void writeVarInt(DataOutputStream outputStream, int value) throws IOException {
        do {
            byte part = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0)
                part |= (byte) 128;
            outputStream.writeByte(part);
        } while (value != 0);
    }

    public synchronized long readVarLong(DataInputStream input) throws IOException {
        lastReadedBytes = 0;

        long out = 0L;
        byte in;
        do {
            in = input.readByte();
            out |= (in & 0x7F) << lastReadedBytes++ * 7;
            if (lastReadedBytes > 10) throw new IOException("VarLong too big!");
        } while ((in & 0x80) == 0x80);

        return out;
    }

    public synchronized void writeVarLong(DataOutputStream outputStream, long value) throws IOException {
        do {
            byte part = (byte) (value & 0x7FL);
            value >>>= 7;
            if (value != 0L)
                part |= (byte) 128;
            outputStream.writeByte(part);
        } while (value != 0L);
    }

    public int getLastReadedBytes() {
        return lastReadedBytes;
    }
}
