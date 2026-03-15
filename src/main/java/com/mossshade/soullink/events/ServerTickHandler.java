package com.mossshade.soullink.events;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.pool.PoolManager;
import com.mossshade.soullink.pool.SharedPoolPayload;
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
			Soullink.LOGGER.debug("Syncing Pool: {}", PoolManager.getPool(minecraftServer));

			String dirtyPlayerUuidString = (PoolManager.getDirtyPlayer() != null) ? PoolManager.getDirtyPlayer().toString() : "";

			for (ServerPlayerEntity serverPlayer : serverWorld.getPlayers()) {
				ServerPlayNetworking.send(serverPlayer, new SharedPoolPayload(
						PoolManager.getPool(minecraftServer).getHealth(),
						PoolManager.getPool(minecraftServer).getFoodLevel(),
						dirtyPlayerUuidString
						)
				);
			}

			PoolManager.clean();
		}
	}

}
