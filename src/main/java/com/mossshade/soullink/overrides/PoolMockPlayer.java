package com.mossshade.soullink.overrides;

import com.mojang.authlib.GameProfile;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class PoolMockPlayer extends ServerPlayerEntity {

	private final SharedPoolManager poolManager;

	public PoolMockPlayer(MinecraftServer server, ServerWorld world, SharedPoolManager poolManager) {
		super(server, world, new GameProfile(UUID.randomUUID(), "SharedPool"), SyncedClientOptions.createDefault());
		this.poolManager = poolManager;
	}


	@Override
	public float getHealth() {
		return this.poolManager.getPoolHealth();
	}

	@Override
	public void heal(float amount) {
		this.poolManager.addHeal(amount);
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		this.poolManager.addDamage(amount);
		return true;
	}


}
