package com.mossshade.soullink.interfaces;

import net.minecraft.server.network.ServerPlayerEntity;

public interface HungerManagerAccess {

	ServerPlayerEntity soullink$getPlayer();

	void soullink$setPlayer(ServerPlayerEntity player);

	float soullink$getExhaustion();

	void soullink$setExhaustion(float exhaustion);

	int soullink$getFoodTickTimer();
	void soullink$setFoodTickTimer(int foodTickTimer);

}
