package com.mossshade.soullink.pool;

import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.WeakHashMap;

public class PoolManagerHolder {

	private static final Map<MinecraftServer, SharedPoolManager> MANAGERS = new WeakHashMap<>();

	public static void init(MinecraftServer server) {
		MANAGERS.put(server, new SharedPoolManager(server));
	}

	public static SharedPoolManager get(MinecraftServer server) {
		return MANAGERS.get(server);
	}

	public static void remove(MinecraftServer server) {
		MANAGERS.remove(server);
	}

}
