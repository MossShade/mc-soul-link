package com.mossshade.soullink.events;

import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTickHandler {

	public static void register(ServerWorld serverWorld) {
		if (serverWorld.isClient()) return;

		MinecraftServer minecraftServer = serverWorld.getServer();
		if (minecraftServer == null) return;

		if (serverWorld.getPlayers().isEmpty()) return;

		SharedPoolManager poolManager = PoolAPI.get(serverWorld);

		poolManager.tickSharedHunger();

		// TODO: Find out how to send a packet only when needed so that we don't clog the network
//		if (!ConfigManager.isDisabled() && poolManager.dirtyTracker.isDirty()) {
//
//			Soullink.LOGGER.debug("Syncing Pool: {}", poolManager);
//
//			String dirtyPlayerUuidString = (poolManager.dirtyTracker.getDirt() == null) ? "" : poolManager.dirtyTracker.getDirt().toString();
//
//			for (ServerPlayerEntity serverPlayer : serverWorld.getPlayers()) {
//				ServerPlayNetworking.send(serverPlayer, new SharedPoolPayload(
//						poolManager.getPoolHealth(),
//						poolManager.getPoolFoodLevel(),
//						poolManager.getPoolSaturationLevel(),
//						poolManager.getPoolExhaustion(),
//						poolManager.getPoolFoodTickTimer(),
//						dirtyPlayerUuidString));
//			}
//
//			poolManager.dirtyTracker.clean();
//		}
	}

}
