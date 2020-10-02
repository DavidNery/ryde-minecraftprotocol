package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketPlayInDisconnect implements ClientBoundPacket {

    private JsonElement message;

    public PacketPlayInDisconnect() {
    }

    private PacketPlayInDisconnect(JsonElement message) {
        this.message = message;
    }

    @Override
    public PacketPlayInDisconnect deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        JsonElement message = new Gson().fromJson(byteUtils.readString(data, StandardCharsets.UTF_8), JsonElement.class);

        return new PacketPlayInDisconnect(message);
    }

    public JsonElement getMessage() {
        return message;
    }
}
