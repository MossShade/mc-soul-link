package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolManager;
import com.mossshade.soullink.utils.Constants;
import com.mossshade.soullink.utils.Helpers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerRespawnHandler {

	public static void register(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		MinecraftServer server = Helpers.getMinecraftServer(newPlayer);
		server.getPlayerManager().broadcast(Text.translatable(Constants.RESPAWN_RESET_MESSAGE), true);
		PoolManager.reset(server);
	}

}
