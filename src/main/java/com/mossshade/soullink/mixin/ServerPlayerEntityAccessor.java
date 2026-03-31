package com.mossshade.soullink.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface ServerPlayerEntityAccessor {

	@Accessor("lastSentHealth")
	void setSyncedHealth(float value);

	@Accessor("lastSentHealth")
	float getSyncedHealth();

}
