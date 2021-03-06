package com.nukkitx.protocol.bedrock.v340.serializer;

import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.serializer.PacketSerializer;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.nukkitx.protocol.bedrock.packet.PlayStatusPacket.Status;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayStatusSerializer_v340 implements PacketSerializer<PlayStatusPacket> {
    public static final PlayStatusSerializer_v340 INSTANCE = new PlayStatusSerializer_v340();


    @Override
    public void serialize(ByteBuf buffer, PlayStatusPacket packet) {
        buffer.writeInt(packet.getStatus().ordinal());
    }

    @Override
    public void deserialize(ByteBuf buffer, PlayStatusPacket packet) {
        packet.setStatus(Status.values()[buffer.readInt()]);
    }
}
