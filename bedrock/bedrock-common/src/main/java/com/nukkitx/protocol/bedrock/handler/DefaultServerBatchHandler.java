package com.nukkitx.protocol.bedrock.handler;

import com.nukkitx.protocol.bedrock.*;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.SubClientLoginPacket;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Collection;

public class DefaultServerBatchHandler extends DefaultBatchHandler {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(DefaultServerBatchHandler.class);
    private final BedrockServer server;

    public DefaultServerBatchHandler(BedrockServer server) {
        this.server = server;
    }

    @Override
    public void handle(BedrockSession session, ByteBuf compressed, Collection<BedrockPacket> packets) {
        for (BedrockPacket packet : packets) {
            if (packet instanceof LoginPacket) {
                ((BedrockServerSession) session).setClientId(packet.getClientId());
            } else if (packet instanceof SubClientLoginPacket) {
                session = ((BedrockServerSession) session).createSubSession(packet.getClientId(), server);
                server.getHandler().onSubSessionCreation((BedrockSubClientServerSession) session);
            } else {
                session = ((BedrockServerSession) session).getSubSessions().get(packet.getClientId());
            }

            if (session.isLogging() && log.isTraceEnabled()) {
                log.trace("Inbound {}: {}", session.getAddress(), packet);
            }

            BedrockPacketHandler handler = session.getPacketHandler();
            boolean release = true;
            try {
                if (handler != null && packet.handle(handler)) {
                    release = false;
                } else {
                    log.debug("Unhandled packet for {}: {}", session.getAddress(), packet);
                }
            } finally {
                if (release) {
                    ReferenceCountUtil.release(packet);
                }
            }
        }
    }
}
