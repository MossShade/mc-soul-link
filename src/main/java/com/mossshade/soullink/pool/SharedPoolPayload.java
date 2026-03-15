package com.mossshade.soullink.pool;

import com.mossshade.soullink.utils.Constants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SharedPoolPayload(Float health, Integer food, String playerUuid) implements CustomPayload {

	public static final Identifier SHARED_POOL_ID = Identifier.of(Constants.MOD_ID, Constants.SHARED_POOL);
	public static final CustomPayload.Id<SharedPoolPayload> ID = new CustomPayload.Id<>(SHARED_POOL_ID);

	public static final PacketCodec<PacketByteBuf, SharedPoolPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.FLOAT, SharedPoolPayload::health,
			PacketCodecs.INTEGER, SharedPoolPayload::food,
			PacketCodecs.STRING, SharedPoolPayload::playerUuid,
			SharedPoolPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

}
