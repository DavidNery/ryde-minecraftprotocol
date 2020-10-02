package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.ChatPosition;
import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketPlayInChatMessage implements ClientBoundPacket {

    private JsonElement message;
    private ChatPosition chatPosition;

    public PacketPlayInChatMessage() {
    }

    private PacketPlayInChatMessage(JsonElement message, ChatPosition chatPosition) {
        this.message = message;
        this.chatPosition = chatPosition;
    }

    @Override
    public PacketPlayInChatMessage deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        JsonElement message = new Gson().fromJson(byteUtils.readString(data, StandardCharsets.UTF_8), JsonElement.class);

        return new PacketPlayInChatMessage(message, ChatPosition.parse(data.readByte()));
    }

    public JsonElement getMessage() {
        return message;
    }

    public ChatPosition getChatPosition() {
        return chatPosition;
    }
}
