package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class ServerTickHandler {

	public static void register(ServerLevel serverWorld) {
		if (serverWorld.isClientSide()) return;

		MinecraftServer minecraftServer = serverWorld.getServer();
		if (minecraftServer == null) return;

		if (serverWorld.players().isEmpty()) return;

		PoolAPI.get(serverWorld).tickSharedHunger();
		PoolAPI.get(serverWorld).propagatePool();
	}

}
