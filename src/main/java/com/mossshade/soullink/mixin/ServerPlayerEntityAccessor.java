package com.mossshade.soullink.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {

	@Accessor("syncedHealth")
	void setSyncedHealth(float value);

	@Accessor("syncedHealth")
	void getSyncedHealth(float value);

}
