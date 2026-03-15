package com.mossshade.soullink.interfaces;

import net.minecraft.server.network.ServerPlayerEntity;

public interface HungerManagerAccess {

	ServerPlayerEntity soullink$getPlayer();

	void soullink$setPlayer(ServerPlayerEntity player);

}
