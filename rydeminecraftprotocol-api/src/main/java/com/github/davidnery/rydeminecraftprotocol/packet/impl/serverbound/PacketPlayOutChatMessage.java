package com.github.davidnery.rydeminecraftprotocol.packet.impl.serverbound;

import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.packet.ServerBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketPlayOutChatMessage implements ServerBoundPacket {

    private final String msg;

    public PacketPlayOutChatMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public void serialize(ByteUtils byteUtils, DataOutputStream output) throws IOException {
        byteUtils.writeString(output, msg, StandardCharsets.UTF_8);
    }
}
