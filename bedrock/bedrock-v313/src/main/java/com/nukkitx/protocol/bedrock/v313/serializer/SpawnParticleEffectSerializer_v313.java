package com.nukkitx.protocol.bedrock.v313.serializer;

import com.nukkitx.protocol.bedrock.packet.SpawnParticleEffectPacket;
import com.nukkitx.protocol.bedrock.v313.BedrockUtils;
import com.nukkitx.protocol.serializer.PacketSerializer;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpawnParticleEffectSerializer_v313 implements PacketSerializer<SpawnParticleEffectPacket> {
    public static final SpawnParticleEffectSerializer_v313 INSTANCE = new SpawnParticleEffectSerializer_v313();

    @Override
    public void serialize(ByteBuf buffer, SpawnParticleEffectPacket packet) {
        buffer.writeByte(packet.getDimensionId());
        BedrockUtils.writeVector3f(buffer, packet.getPosition());
        BedrockUtils.writeString(buffer, packet.getIdentifier());
    }

    @Override
    public void deserialize(ByteBuf buffer, SpawnParticleEffectPacket packet) {
        packet.setDimensionId(buffer.readUnsignedByte());
        packet.setPosition(BedrockUtils.readVector3f(buffer));
        packet.setIdentifier(BedrockUtils.readString(buffer));
    }
}
