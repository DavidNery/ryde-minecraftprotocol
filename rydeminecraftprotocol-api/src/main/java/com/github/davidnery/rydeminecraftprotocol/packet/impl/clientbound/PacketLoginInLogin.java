package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketLoginInLogin implements ClientBoundPacket {

    private UUID uuid;
    private String name;

    public PacketLoginInLogin() {
    }

    public PacketLoginInLogin(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public PacketLoginInLogin deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        return new PacketLoginInLogin(
                UUID.fromString(byteUtils.readString(data, StandardCharsets.UTF_8)),
                byteUtils.readString(data, StandardCharsets.UTF_8)
        );
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

}
