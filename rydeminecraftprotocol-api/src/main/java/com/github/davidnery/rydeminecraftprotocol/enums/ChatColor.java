package com.github.davidnery.rydeminecraftprotocol.enums;

public enum ChatColor {

    BLACK("0x000000"),
    DARK_BLUE("0x0000aa"),
    DARK_GREEN("0x00aa00"),
    DARK_AQUA("0x00aaaa"),
    DARK_RED("0xaa0000"),
    DARK_PURPLE("0xaa00aa"),
    GOLD("0xffaa00"),
    GRAY("0xaaaaaa"),
    DARK_GRAY("0x555555"),
    BLUE("0x5555ff"),
    GREEN("0x55ff55"),
    AQUA("0x55ffff"),
    RED("0xff5555"),
    LIGHT_PURPLE("0xff55ff"),
    YELLOW("0xffff55"),
    WHITE("0xffffff");

    private final String color;

    ChatColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static ChatColor parse(String color) {
        String finalName = color.toUpperCase();
        for (ChatColor chatColor : values())
            if (chatColor.name().equalsIgnoreCase(finalName)) return chatColor;

        return WHITE;
    }
}