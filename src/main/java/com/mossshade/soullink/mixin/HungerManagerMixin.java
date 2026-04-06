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
	private void tick(ServerPlayer player, CallbackInfo ci) {
		if (player == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		PoolAPI.get(player).tickSharedHunger();

		ci.cancel();
	}

	@Inject(method = "add", at = @At("HEAD"), cancellable = true)
	private void add(int food, float saturation, CallbackInfo ci) {
		ServerPlayer player = this.soullink$getPlayer();
		if (player == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("eat {} nutrition and {} saturation for player {}", food, saturation, player);

		poolManager.dirtyTracker.markDirty(player.getUUID());
		poolManager.addFood(food, saturation);

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
	public void soullink$setExhaustion(float exhaustionLevel) {
		this.exhaustionLevel = exhaustionLevel;
	}

	@Override
	public int soullink$getFoodTickTimer() {
		return this.tickTimer;
	}

	@Override
	public void soullink$setFoodTickTimer(int tickTimer) {
		this.tickTimer = tickTimer;
	}
}
