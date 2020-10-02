package com.github.davidnery.rydeminecraftprotocol.runnables;

import com.github.davidnery.rydeminecraftprotocol.enums.ConnectionStatus;
import com.github.davidnery.rydeminecraftprotocol.enums.PacketDirection;
import com.github.davidnery.rydeminecraftprotocol.models.Client;
import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketLoginInDisconnect;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketLoginInLogin;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketLoginInSetCompression;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketPlayInKeepAlive;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.serverbound.PacketPlayOutKeepAlive;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;
import com.github.davidnery.rydeminecraftprotocol.utils.CompressionUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.zip.DataFormatException;

public class ClientRunnable implements Runnable {

    private final Socket socket;

    private final ByteUtils byteUtils;
    private final DataInputStream inputStream;

    private final Client client;

    private int maxPacketSize;

    public ClientRunnable(Client client) {
        this.client = client;

        this.socket = client.getSocket();

        this.byteUtils = client.getByteUtils();
        this.inputStream = client.getInputStream();

        maxPacketSize = -1;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                if (inputStream.available() == 0) continue;

                // https://wiki.vg/index.php?title=Protocol#Packet_format
                int bytesToRead = byteUtils.readVarInt(inputStream), dataLength, responseId;

                DataInputStream realData = inputStream;
                if (maxPacketSize != -1 && (dataLength = byteUtils.readVarInt(inputStream)) > maxPacketSize) { // length of uncompressed data
                    byte[] packet = new byte[bytesToRead - byteUtils.getLastReadedBytes()]; // length of compressed data
                    inputStream.read(packet, 0, packet.length);
                    packet = CompressionUtils.decompress(packet, dataLength);

                    DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(packet));
                    responseId = byteUtils.readVarInt(dataInput);
                    realData = dataInput;
                } else {
                    bytesToRead -= byteUtils.getLastReadedBytes(); // remove readed bytes by dataLength
                    responseId = byteUtils.readVarInt(inputStream);
                    bytesToRead -= byteUtils.getLastReadedBytes();
                }

                if (bytesToRead <= 0) continue;

                ClientBoundPacket packet = (ClientBoundPacket) client.getConnectionStatus().getById(PacketDirection.CLIENTBOUND, responseId);
                if (packet == null) {
                    if (realData == inputStream)
                        inputStream.skip(bytesToRead);
                } else {
                    packet = packet.deserialize(byteUtils, realData);
                    client.notify(packet);

                    if (packet instanceof PacketLoginInDisconnect) {
                        client.disconnect();
                        break;
                    } else if (packet instanceof PacketLoginInLogin) {
                        client.setConnectionStatus(ConnectionStatus.PLAY);
                    } else if (packet instanceof PacketLoginInSetCompression) {
                        maxPacketSize = ((PacketLoginInSetCompression) packet).getMaxPacketSize();
                        client.setMaximumPacketSize(maxPacketSize);
                    } else if (packet instanceof PacketPlayInKeepAlive) {
                        client.sendPacket(new PacketPlayOutKeepAlive(((PacketPlayInKeepAlive) packet).getEntityId()));
                    }
                }
            } catch (IOException | DataFormatException e) {
                e.printStackTrace();
                try {
                    client.disconnect();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
