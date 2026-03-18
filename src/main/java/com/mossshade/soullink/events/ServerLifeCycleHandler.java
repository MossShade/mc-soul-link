package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolManagerHolder;
import net.minecraft.server.MinecraftServer;

public class ServerLifeCycleHandler {

	public static void registerStarted(MinecraftServer server) {
		PoolManagerHolder.init(server);
	}

	public static void registerStopped(MinecraftServer server) {
		PoolManagerHolder.remove(server);
	}

}
