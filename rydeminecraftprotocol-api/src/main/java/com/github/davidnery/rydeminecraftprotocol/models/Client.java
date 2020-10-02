package com.github.davidnery.rydeminecraftprotocol.models;

import com.github.davidnery.rydeminecraftprotocol.enums.ConnectionStatus;
import com.github.davidnery.rydeminecraftprotocol.enums.PacketDirection;
import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.HandShakeState;
import com.github.davidnery.rydeminecraftprotocol.packet.ServerBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake.PacketLoginOutStart;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake.PacketOutHandShake;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.handshake.PacketStatusOutPing;
import com.github.davidnery.rydeminecraftprotocol.runnables.ClientRunnable;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;
import com.github.davidnery.rydeminecraftprotocol.utils.CompressionUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends Subject {

    private final String ip;
    private final int port;

    private final ByteUtils byteUtils;

    private volatile Socket socket;

    private volatile DataOutputStream outputStream;
    private volatile DataInputStream inputStream;

    private volatile int maximumPacketSize;

    private volatile ConnectionStatus connectionStatus;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;

        this.maximumPacketSize = -1;
        this.byteUtils = new ByteUtils();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ByteUtils getByteUtils() {
        return byteUtils;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public void setMaximumPacketSize(int maximumPacketSize) {
        this.maximumPacketSize = maximumPacketSize;
    }

    private void connect() throws IOException {
        socket = new Socket(ip, port);

        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }

    public void sendPacket(ServerBoundPacket packet) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(buffer);
        byteUtils.writeVarInt(stream, connectionStatus.getId(PacketDirection.SERVERBOUND, packet));
        packet.serialize(byteUtils, stream);

        byte[] data = buffer.toByteArray();

        if (maximumPacketSize != -1) { // already received SetCompression packet
            // perhaps need to be compressed
            if (data.length >= maximumPacketSize) { // need to be compressed
                byte[] compressedData = CompressionUtils.compress(data);

                stream = new DataOutputStream(new ByteArrayOutputStream());
                byteUtils.writeVarInt(stream, data.length);

                byteUtils.writeVarInt(outputStream, stream.size() + compressedData.length);
                byteUtils.writeVarInt(outputStream, data.length);
                outputStream.write(compressedData);
            } else { // not need to be compressed :P
                byteUtils.writeVarInt(outputStream, data.length + 1); // 1 is the length of "fake" data length
                byteUtils.writeVarInt(outputStream, 0);
                outputStream.write(data);
            }
        } else { // not need to be compressed
            byteUtils.writeVarInt(outputStream, data.length);
            outputStream.write(data);
        }
        outputStream.flush();
    }

    public void login(String nick, int protocol) throws IOException {
        connect();

        connectionStatus = ConnectionStatus.HANDSHAKE;
        PacketOutHandShake packetOutHandShake = new PacketOutHandShake(ip, protocol, port, HandShakeState.LOGIN);
        sendPacket(packetOutHandShake);

        connectionStatus = ConnectionStatus.LOGIN;

        PacketLoginOutStart packetLoginOutStart = new PacketLoginOutStart(nick);
        sendPacket(packetLoginOutStart);

        new Thread(new ClientRunnable(this)).start();
    }

    public String ping(int version) throws IOException {
        connect();

        connectionStatus = ConnectionStatus.HANDSHAKE;

        PacketOutHandShake packetOutHandShake = new PacketOutHandShake(
                ip, version, port, HandShakeState.STATUS
        );
        sendPacket(packetOutHandShake);

        connectionStatus = ConnectionStatus.STATUS;

        PacketStatusOutPing packetStatusOutPing = new PacketStatusOutPing();
        sendPacket(packetStatusOutPing);

        byteUtils.readVarInt(inputStream); // packetSize
        byteUtils.readVarInt(inputStream); // packetId
        String response = byteUtils.readString(inputStream, StandardCharsets.UTF_8);

        disconnect();

        connectionStatus = null;

        return response;
    }
}
