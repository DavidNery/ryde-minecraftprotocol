package com.github.davidnery.rydeminecraftprotocol.enums.minecraft;

public enum HandShakeState {

    STATUS(1),
    LOGIN(2);

    private final int state;

    HandShakeState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
