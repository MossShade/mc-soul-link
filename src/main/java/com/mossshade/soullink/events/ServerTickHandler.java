package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTickHandler {

	public static void register(ServerWorld serverWorld) {
		if (serverWorld.isClient()) return;

		MinecraftServer minecraftServer = serverWorld.getServer();
		if (minecraftServer == null) return;

		if (serverWorld.getPlayers().isEmpty()) return;

		PoolAPI.get(serverWorld).tickSharedHunger();
	}

}
