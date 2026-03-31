package com.mossshade.soullink.overrides;

import com.mojang.authlib.GameProfile;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import java.util.UUID;

public class PoolMockPlayer extends ServerPlayer {

	private final SharedPoolManager poolManager;

	public PoolMockPlayer(MinecraftServer server, ServerLevel world, SharedPoolManager poolManager) {
		super(server, world, new GameProfile(UUID.randomUUID(), "SharedPool"), ClientInformation.createDefault());
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
	public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		this.poolManager.addDamage(amount);
		return true;
	}


}
