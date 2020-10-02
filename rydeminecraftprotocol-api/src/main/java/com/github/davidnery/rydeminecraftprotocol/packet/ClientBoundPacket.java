package com.github.davidnery.rydeminecraftprotocol.packet;

import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;

public interface ClientBoundPacket extends Packet {

    ClientBoundPacket deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException;

}
