package com.nukkitx.protocol.bedrock;

import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.protocol.MinecraftServerSession;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializers;
import io.netty.channel.EventLoop;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

import javax.annotation.Nullable;

public class BedrockServerSession extends BedrockSession implements MinecraftServerSession<BedrockPacket> {
    @Getter
    private final Int2ObjectMap<BedrockSession> subSessions;
    protected int clientId;

    public BedrockServerSession(RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        super(connection, eventLoop, serializer);
        this.subSessions = new Int2ObjectOpenHashMap<>();
    }

    @Override
    public void disconnect() {
        this.disconnect(null, true);
    }

    public void disconnect(@Nullable String reason) {
        this.disconnect(reason, false);
    }

    public void disconnect(@Nullable String reason, boolean hideReason) {
        this.checkForClosed();

        DisconnectPacket packet = new DisconnectPacket();
        if (reason == null || hideReason) {
            packet.setMessageSkipped(true);
            reason = "disconnect.disconnected";
        }
        packet.setKickMessage(reason);
        this.sendPacketImmediately(packet);
    }

    public BedrockSubClientServerSession createSubSession(int clientId, BedrockServer server) {
        BedrockSubClientServerSession serverSession = new BedrockSubClientServerSession(this, clientId, (RakNetSession) this.connection, server.eventLoopGroup.next(),
                BedrockWrapperSerializers.getSerializer(((RakNetSession) this.connection).getProtocolVersion()));
        this.subSessions.put(clientId, serverSession);
        return serverSession;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
        this.subSessions.put(clientId, this);
    }
}
