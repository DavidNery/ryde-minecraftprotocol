package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketLoginInSetCompression implements ClientBoundPacket {

    private int maxPacketSize;

    public PacketLoginInSetCompression() {
    }

    public PacketLoginInSetCompression(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    @Override
    public ClientBoundPacket deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        return new PacketLoginInSetCompression(byteUtils.readVarInt(data));
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }
}
