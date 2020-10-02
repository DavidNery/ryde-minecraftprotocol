package com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake;

import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.HandShakeState;
import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.packet.ServerBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketOutHandShake implements ServerBoundPacket {

    private final String host;
    private final int protocolVersion, port;

    private final HandShakeState state;

    public PacketOutHandShake(String host, int protocolVersion, int port, HandShakeState state) {
        this.host = host;
        this.protocolVersion = protocolVersion;
        this.port = port;
        this.state = state;
    }

    @Override
    public void serialize(ByteUtils byteUtils, DataOutputStream outputStream) throws IOException {
        byteUtils.writeVarInt(outputStream, protocolVersion);
        byteUtils.writeString(outputStream, host, StandardCharsets.UTF_8);
        outputStream.writeShort(port);
        byteUtils.writeVarInt(outputStream, state.getState());
    }
}
