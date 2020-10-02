package com.github.davidnery.rydeminecraftprotocol.models;

import com.github.davidnery.rydeminecraftprotocol.packet.Packet;

public interface Observer<P extends Packet> {

    void receive(P packet);

}
