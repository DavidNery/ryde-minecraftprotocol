package com.github.davidnery.rydeminecraftprotocol.packet.impl.serverbound;

import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.packet.ServerBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayOutKeepAlive implements ServerBoundPacket {

    private final int entityId;

    public PacketPlayOutKeepAlive(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void serialize(ByteUtils byteUtils, DataOutputStream output) throws IOException {
        byteUtils.writeVarInt(output, entityId);
    }
}
