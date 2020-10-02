package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketLoginInDisconnect implements ClientBoundPacket {

    private String message;

    public PacketLoginInDisconnect() {
    }

    private PacketLoginInDisconnect(String message) {
        this.message = message;
    }

    @Override
    public PacketLoginInDisconnect deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        return new PacketLoginInDisconnect(byteUtils.readString(data, StandardCharsets.UTF_8));
    }

    public String getMessage() {
        return message;
    }
}
