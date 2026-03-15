package com.mossshade.soullink.pool;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.damage.SoullinkDamageTypes;
import com.mossshade.soullink.mixin.ServerPlayerEntityAccessor;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class PoolManager {

	private static boolean dirty = false;

	private static SharedPoolState pool;

	private static UUID dirtyPlayerUuid;

	public static SharedPoolState getPool(MinecraftServer minecraftServer) {
		if (pool == null) {
			pool = SharedPoolState.getServerState(minecraftServer);
		}

		return pool;
	}

	public static void propagatePool(MinecraftServer server) {
		PoolManager.getPool(server).markDirty();
		PoolManager.markDirty();

		syncPlayers(server);
	}

	public static void reset(MinecraftServer server) {
		SharedPoolState pool = PoolManager.getPool(server);

		cleanDirtyPlayer();

		setPool(server, pool.getMaxHealth(), pool.getMaxFoodLevel());
	}

	public static void setPool(MinecraftServer server, float health, int food) {
		SharedPoolState pool = PoolManager.getPool(server);

		pool.setHealth(health);
		pool.setFoodLevel(food);

		propagatePool(server);
	}

	public static void propagateHealth(MinecraftServer server, float health) {
		PoolManager.getPool(server).setHealth(health);

		propagatePool(server);
	}

	public static void propagateDamage(MinecraftServer server, float damage) {
		propagateHealth(server, PoolManager.getPool(server).getHealth() - damage);
	}

	public static void propagateHeal(MinecraftServer server, float heal) {
		propagateHealth(server, PoolManager.getPool(server).getHealth() + heal);
	}

	public static void propagateFood(MinecraftServer server, float food) {
		PoolManager.getPool(server).setFoodLevel((int) food);

		propagatePool(server);
	}

	public static void propagateEating(MinecraftServer server, float foodEaten) {
		propagateFood(server, PoolManager.getPool(server).getFoodLevel() + foodEaten);
	}

	public static void killEveryone(MinecraftServer minecraftServer, DamageSource source) {
		for (ServerWorld world : minecraftServer.getWorlds()) {
			for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
				if (dirtyPlayerUuid != null && dirtyPlayerUuid == serverPlayerEntity.getUuid()) continue;

				serverPlayerEntity.getDamageTracker().onDamage(SoullinkDamageTypes.SOUL_FRAGMENTATION, 0f);
				serverPlayerEntity.onDeath(SoullinkDamageTypes.SOUL_FRAGMENTATION);
			}
		}
	}

	public static void syncPlayers(MinecraftServer minecraftServer) {
		for (ServerWorld world : minecraftServer.getWorlds()) {
			for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
				syncPlayer(serverPlayerEntity, PoolManager.getPool(minecraftServer).getHealth(), PoolManager.getPool(minecraftServer).getFoodLevel());
			}
		}
	}

	public static void syncPlayer(ServerPlayerEntity serverPlayerEntity, float health, int foodLevel) {
		if (!ConfigManager.isHealthDisabled()) {
			serverPlayerEntity.setHealth(health);
			((ServerPlayerEntityAccessor) serverPlayerEntity).setSyncedHealth(health);
		}
		if (!ConfigManager.isFoodDisabled()) {
			serverPlayerEntity.getHungerManager().setFoodLevel(foodLevel);
		}
	}

	public static boolean isDirty() {
		return dirty;
	}

	public static void markDirty() {
		PoolManager.dirty = true;
	}

	public static UUID getDirtyPlayer() {
		return dirtyPlayerUuid;
	}

	public static void setDirtyPlayer(ServerPlayerEntity sourcePlayer) {
		PoolManager.dirtyPlayerUuid = sourcePlayer.getUuid();
	}

	public static void cleanDirtyPlayer() {
		PoolManager.dirtyPlayerUuid = null;
	}

	public static void clean() {
		PoolManager.dirty = false;

		PoolManager.dirtyPlayerUuid = null;
	}

}
