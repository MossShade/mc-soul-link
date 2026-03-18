package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import com.mossshade.soullink.pool.SharedPoolPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayerJoinHandler {

	public static void register(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		SharedPoolManager poolManager = PoolAPI.get(server);

		poolManager.syncEntity(handler.player);

		sender.sendPacket(new SharedPoolPayload(
				poolManager.getPoolHealth(),
				poolManager.getPoolFoodLevel(),
				poolManager.getPoolSaturationLevel(),
				poolManager.getPoolExhaustion(),
				poolManager.getPoolFoodTickTimer(),
				""
		));
	}

}
