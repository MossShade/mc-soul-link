package com.mossshade.soullink.events;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.pool.PoolManager;
import com.mossshade.soullink.pool.SharedPoolPayload;
import com.mossshade.soullink.pool.SharedPoolState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ServerTickHandler {

	public static void register(ServerWorld serverWorld) {
		if (serverWorld.isClient()) return;

		MinecraftServer minecraftServer = serverWorld.getServer();
		if (minecraftServer == null) return;

		if (PoolManager.isDirty()) {
			SharedPoolState pool = PoolManager.getPool(minecraftServer);
			Soullink.LOGGER.debug("Syncing Pool: {}", pool);

			Float poolHealth = (ConfigManager.isHealthDisabled()) ? -1f : pool.getHealth();
			Integer poolHunger = (ConfigManager.isFoodDisabled()) ? -1 : pool.getFoodLevel();
			String dirtyPlayerUuidString = (PoolManager.getDirtyPlayer() != null) ? PoolManager.getDirtyPlayer().toString() : "";

			for (ServerPlayerEntity serverPlayer : serverWorld.getPlayers()) {
				ServerPlayNetworking.send(serverPlayer, new SharedPoolPayload(poolHealth, poolHunger, dirtyPlayerUuidString));
			}

			PoolManager.clean();
		}
	}

}
