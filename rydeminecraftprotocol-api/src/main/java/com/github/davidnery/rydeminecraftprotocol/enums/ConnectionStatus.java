package com.github.davidnery.rydeminecraftprotocol.enums;

import com.github.davidnery.rydeminecraftprotocol.packet.Packet;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.*;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake.PacketLoginOutStart;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake.PacketOutHandShake;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake.PacketStatusOutPing;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.serverbound.PacketPlayOutChatMessage;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.serverbound.PacketPlayOutKeepAlive;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionStatus {

    HANDSHAKE {{
        this.register(PacketDirection.SERVERBOUND, 0x00, PacketOutHandShake.class);
    }},
    STATUS {{
        this.register(PacketDirection.SERVERBOUND, 0x00, PacketStatusOutPing.class);
    }},
    LOGIN {{
        this.register(PacketDirection.SERVERBOUND, 0x00, PacketLoginOutStart.class);
        this.register(PacketDirection.CLIENTBOUND, 0x00, PacketLoginInDisconnect.class);
        this.register(PacketDirection.CLIENTBOUND, 0x02, PacketLoginInLogin.class);
        this.register(PacketDirection.CLIENTBOUND, 0x03, PacketLoginInSetCompression.class);
    }},
    PLAY {{
        this.register(PacketDirection.SERVERBOUND, 0x00, PacketPlayOutKeepAlive.class);
        this.register(PacketDirection.SERVERBOUND, 0x01, PacketPlayOutChatMessage.class);
        this.register(PacketDirection.CLIENTBOUND, 0x00, PacketPlayInKeepAlive.class);
        this.register(PacketDirection.CLIENTBOUND, 0x01, PacketPlayInJoinGame.class);
        this.register(PacketDirection.CLIENTBOUND, 0x02, PacketPlayInChatMessage.class);
        this.register(PacketDirection.CLIENTBOUND, 0x40, PacketPlayInDisconnect.class);
    }};

    private final HashMap<PacketDirection, HashMap<Integer, Class<? extends Packet>>> registeredPackets;

    ConnectionStatus() {
        registeredPackets = new HashMap<>();
    }

    protected void register(PacketDirection direction, int id, Class<? extends Packet> packet) {
        HashMap<Integer, Class<? extends Packet>> packets = registeredPackets.computeIfAbsent(direction, k ->
                new HashMap<>()
        );

        packets.putIfAbsent(id, packet);
    }

    public Packet getById(PacketDirection direction, int packetId) {
        HashMap<Integer, Class<? extends Packet>> packets = registeredPackets.get(direction);
        Class<? extends Packet> packetClass = packets.get(packetId);

        if (packetClass != null) {
            try {
                return packetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public int getId(PacketDirection direction, Packet packet) {
        Class<? extends Packet> clazz = packet.getClass();
        for (Map.Entry<Integer, Class<? extends Packet>> entry : registeredPackets.get(direction).entrySet())
            if (entry.getValue() == clazz) return entry.getKey();

        return -1;
    }

}
