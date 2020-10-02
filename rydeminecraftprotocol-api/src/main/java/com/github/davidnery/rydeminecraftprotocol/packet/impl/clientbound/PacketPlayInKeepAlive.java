package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketPlayInKeepAlive implements ClientBoundPacket {

    private int entityId;

    public PacketPlayInKeepAlive() {
    }

    private PacketPlayInKeepAlive(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public ClientBoundPacket deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        return new PacketPlayInKeepAlive(byteUtils.readVarInt(data));
    }

    public int getEntityId() {
        return entityId;
    }
}
