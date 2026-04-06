package com.mossshade.soullink.events;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.server.MinecraftServer;

public class ServerTickHandler {

	public static void register(MinecraftServer minecraftServer) {
		if (minecraftServer.isPaused()) return;
		if (ConfigManager.isDisabled()) return;

		PoolAPI.get(minecraftServer).propagatePool();
	}
}
