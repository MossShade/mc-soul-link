package com.mossshade.soullink.pool;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PoolAPI {

	public static SharedPoolManager get(MinecraftServer server) {
		return PoolManagerHolder.get(server);
	}

	public static SharedPoolManager get(ServerLevel world) {
		return get(world.getServer());
	}

	public static SharedPoolManager get(ServerPlayer player) {
		return get(player.level());
	}

}
