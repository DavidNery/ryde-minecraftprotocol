package com.github.davidnery.rydeminecraftprotocol.models;

import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Subject {

    protected final HashMap<Class<? extends ClientBoundPacket>, List<Observer<? extends ClientBoundPacket>>> observers;

    public Subject() {
        this.observers = new HashMap<>();
    }

    public <P extends ClientBoundPacket, O extends Observer<P>> void subscribe(Class<P> packetClass, O observer) {
        observers.computeIfAbsent(packetClass, key -> new ArrayList<>()).add(observer);
    }

    public <P extends ClientBoundPacket> void notify(P packet) {
        List<Observer<? extends ClientBoundPacket>> list = observers.get(packet.getClass());
        if (list != null) list.forEach(observer -> ((Observer<P>) observer).receive(packet));
    }

}
