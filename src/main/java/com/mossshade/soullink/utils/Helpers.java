package com.mossshade.soullink.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Helpers {

	public static MinecraftServer getMinecraftServer(ServerPlayerEntity player) {
		MinecraftServer minecraftServer = player.getEntityWorld().getServer();
		assert minecraftServer != null;
		return minecraftServer;
	}

}
