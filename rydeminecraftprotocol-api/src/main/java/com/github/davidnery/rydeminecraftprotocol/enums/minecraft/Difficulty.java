package com.github.davidnery.rydeminecraftprotocol.enums.minecraft;

public enum Difficulty {

    PEACEFUL((byte) 0),
    EASY((byte) 1),
    NORMAL((byte) 2),
    HARD((byte) 3);

    private final byte id;

    Difficulty(byte id) {
        this.id = id;
    }

    public static Difficulty parse(byte id) {
        for (Difficulty difficulty : values())
            if (difficulty.id == id) return difficulty;

        return null;
    }
}
