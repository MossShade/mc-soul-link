package com.mossshade.soullink.events;

import com.mossshade.soullink.Constants;
import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerRespawnHandler {

	public static void register(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		MinecraftServer server = newPlayer.level().getServer();
		if (server == null) return;

		server.getPlayerList().broadcastSystemMessage(Component.translatable(Constants.RESPAWN_RESET_MESSAGE), true);
		PoolAPI.get(newPlayer).reset();
	}

}
