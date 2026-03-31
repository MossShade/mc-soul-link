package com.mossshade.soullink.interfaces;

import net.minecraft.server.level.ServerPlayer;

public interface HungerManagerAccess {

	ServerPlayer soullink$getPlayer();

	void soullink$setPlayer(ServerPlayer player);

	float soullink$getExhaustion();

	void soullink$setExhaustion(float exhaustion);

	int soullink$getFoodTickTimer();
	void soullink$setFoodTickTimer(int foodTickTimer);

}
