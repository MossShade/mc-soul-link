package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolManager;
import com.mossshade.soullink.pool.SharedPoolPayload;
import com.mossshade.soullink.pool.SharedPoolState;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayerJoinHandler {

	public static void register(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		SharedPoolState pool = PoolManager.getPool(server);

		PoolManager.syncPlayer(handler.player, pool.getHealth(), pool.getFoodLevel());

		sender.sendPacket(new SharedPoolPayload(pool.getHealth(), pool.getFoodLevel(), ""));
	}

}
