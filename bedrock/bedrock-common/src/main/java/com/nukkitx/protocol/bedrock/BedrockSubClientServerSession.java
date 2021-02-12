package com.nukkitx.protocol.bedrock;

import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.protocol.MinecraftServerSession;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import io.netty.channel.EventLoop;

import javax.annotation.Nullable;

public class BedrockSubClientServerSession extends BedrockSession implements MinecraftServerSession<BedrockPacket> {
    private final int clientId;

    BedrockSubClientServerSession(int clientId, RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        super(connection, eventLoop, serializer);
        this.clientId = clientId;
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
        packet.setSenderId(clientId);
        this.sendPacketImmediately(packet);
    }

    @Override
    public void sendPacket(BedrockPacket packet) {
        packet.setSenderId(clientId);
        super.sendPacket(packet);
    }

    @Override
    public void sendPacketImmediately(BedrockPacket packet) {
        packet.setSenderId(clientId);
        super.sendPacketImmediately(packet);
    }
}
