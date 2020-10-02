package com.github.davidnery.rydeminecraftprotocol.enums.minecraft;

public enum GameMode {

    SURVIVAL((byte) 0),
    CREATIVE((byte) 1),
    ADVENTURE((byte) 2),
    SPECTATOR((byte) 3);

    private final byte id;

    GameMode(byte id) {
        this.id = id;
    }

    public static GameMode parse(byte id) {
        for (GameMode gameMode : values())
            if (gameMode.id == id) return gameMode;

        return null;
    }
}
