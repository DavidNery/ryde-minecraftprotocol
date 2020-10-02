package com.github.davidnery.rydeminecraftprotocol.enums.minecraft;

public enum Dimension {

    NETHER((byte) -1),
    OVERWORLD((byte) 0),
    END((byte) 1);

    private final byte id;

    Dimension(byte id) {
        this.id = id;
    }

    public static Dimension parse(byte id) {
        for (Dimension dimension : values())
            if (dimension.id == id) return dimension;

        return null;
    }
}
