package com.github.davidnery.rydeminecraftprotocol.enums.minecraft;

public enum ChatPosition {

    CHAT((byte) 0),
    SYSTEMMESSAGE((byte) 1),
    HOTBAR((byte) 2);

    private final byte id;

    ChatPosition(byte id) {
        this.id = id;
    }

    public static ChatPosition parse(byte id) {
        for (ChatPosition chatPosition : values())
            if (chatPosition.id == id) return chatPosition;

        return null;
    }
}
