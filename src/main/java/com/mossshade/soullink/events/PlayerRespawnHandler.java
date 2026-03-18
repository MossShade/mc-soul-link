package com.mossshade.soullink.events;

import com.mossshade.soullink.Constants;
import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerRespawnHandler {

	public static void register(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		MinecraftServer server = newPlayer.getEntityWorld().getServer();
		if (server == null) return;

		server.getPlayerManager().broadcast(Text.translatable(Constants.RESPAWN_RESET_MESSAGE), true);
		PoolAPI.get(newPlayer).reset();
	}

}
