package com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake;

import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.packet.ServerBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketLoginOutStart implements ServerBoundPacket {

    private final String name;

    public PacketLoginOutStart(String name) {
        this.name = name;
    }

    @Override
    public void serialize(ByteUtils byteUtils, DataOutputStream output) throws IOException {
        byteUtils.writeString(output, name, StandardCharsets.UTF_8);
    }
}
