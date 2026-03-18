package com.mossshade.soullink.pool;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PoolAPI {

	public static SharedPoolManager get(MinecraftServer server) {
		return PoolManagerHolder.get(server);
	}

	public static SharedPoolManager get(ServerWorld world) {
		return get(world.getServer());
	}

	public static SharedPoolManager get(ServerPlayerEntity player) {
		return get(player.getEntityWorld());
	}

}
