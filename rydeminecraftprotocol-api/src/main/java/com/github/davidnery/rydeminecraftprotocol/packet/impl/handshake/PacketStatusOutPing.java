package com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake;

import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.packet.ServerBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class PacketStatusOutPing implements ServerBoundPacket {

    @Override
    public void serialize(ByteUtils byteUtils, DataOutputStream output) throws IOException {
    }
}
