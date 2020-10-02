package com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound;

import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.Difficulty;
import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.Dimension;
import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.GameMode;
import com.github.davidnery.rydeminecraftprotocol.enums.minecraft.LevelType;
import com.github.davidnery.rydeminecraftprotocol.packet.ClientBoundPacket;
import com.github.davidnery.rydeminecraftprotocol.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketPlayInJoinGame implements ClientBoundPacket {

    private int entityId, maxPlayers;
    private GameMode gameMode;
    private Dimension dimension;
    private Difficulty difficulty;
    private LevelType levelType;
    private boolean reducedDebug;

    public PacketPlayInJoinGame() {
    }

    private PacketPlayInJoinGame(int entityId, int maxPlayers,
                                 GameMode gameMode, Dimension dimension, Difficulty difficulty,
                                 LevelType levelType, boolean reducedDebug) {
        this.entityId = entityId;
        this.maxPlayers = maxPlayers;
        this.gameMode = gameMode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.levelType = levelType;
        this.reducedDebug = reducedDebug;
    }

    @Override
    public PacketPlayInJoinGame deserialize(ByteUtils byteUtils, DataInputStream data) throws IOException {
        int entityId = data.readInt();
        GameMode gameMode = GameMode.parse((byte) data.readUnsignedByte());
        Dimension dimension = Dimension.parse(data.readByte());
        Difficulty difficulty = Difficulty.parse((byte) data.readUnsignedByte());
        int maxPlayers = data.readUnsignedByte();

        LevelType levelType = LevelType.valueOf(byteUtils.readString(data, StandardCharsets.UTF_8).toUpperCase());
        boolean reducedDebug = data.readBoolean();

        return new PacketPlayInJoinGame(entityId, maxPlayers, gameMode, dimension, difficulty, levelType, reducedDebug);
    }

    public int getEntityId() {
        return entityId;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public LevelType getLevelType() {
        return levelType;
    }

    public boolean isReducedDebug() {
        return reducedDebug;
    }
}
