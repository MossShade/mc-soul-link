package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.server.MinecraftServer;

public class ServerTickHandler {

	public static void register(MinecraftServer minecraftServer) {
		if (minecraftServer.getPlayerCount() <= 0) return;

		PoolAPI.get(minecraftServer).tickSharedHunger();
		PoolAPI.get(minecraftServer).propagatePool();
	}
}
