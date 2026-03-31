package com.mossshade.soullink.mixin;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class HungerManagerMixin implements HungerManagerAccess {

	@Shadow
	private float exhaustionLevel;
	@Shadow
	private int tickTimer;
	@Unique
	private ServerPlayer soullink$player;

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void update(ServerPlayer player, CallbackInfo ci) {
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		ci.cancel();
	}

	@Inject(method = "add", at = @At("HEAD"), cancellable = true)
	private void eat(int nutrition, float saturation, CallbackInfo ci) {
		ServerPlayer player = this.soullink$getPlayer();
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("eat {} nutrition and {} saturation for player {}", nutrition, saturation, player);

		poolManager.dirtyTracker.markDirty(player.getUUID());
		poolManager.addFood(nutrition, saturation);

		ci.cancel();
	}

	@Override
	public ServerPlayer soullink$getPlayer() {
		return soullink$player;
	}

	@Override
	public void soullink$setPlayer(ServerPlayer player) {
		this.soullink$player = player;
	}

	@Override
	public float soullink$getExhaustion() {
		return this.exhaustionLevel;
	}

	@Override
	public void soullink$setExhaustion(float exhaustion) {
		this.exhaustionLevel = exhaustion;
	}

	@Override
	public int soullink$getFoodTickTimer() {
		return this.tickTimer;
	}

	@Override
	public void soullink$setFoodTickTimer(int foodTickTimer) {
		this.tickTimer = foodTickTimer;
	}
}
