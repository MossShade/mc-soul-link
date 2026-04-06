package com.mossshade.soullink.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface ServerPlayerEntityAccessor {

	@Accessor("lastSentHealth")
	void setLastSentHealth(float value);

	@Accessor("lastSentHealth")
	float getLastSentHealth();

	@Accessor("lastSentFood")
	void setLastSentFood(int value);

	@Accessor("lastFoodSaturationZero")
	void setLastFoodSaturationZero(boolean value);

}
