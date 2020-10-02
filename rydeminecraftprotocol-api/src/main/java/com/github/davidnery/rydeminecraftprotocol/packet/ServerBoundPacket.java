package com.github.davidnery.rydeminecraftprotocol.packet;

import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ServerBoundPacket extends Packet {

    void serialize(ByteUtils byteUtils, DataOutputStream output) throws IOException;

}
