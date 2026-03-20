package com.mossshade.soullink.mixin;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements HungerManagerAccess {

	@Shadow
	private float exhaustion;
	@Shadow
	private int foodTickTimer;
	@Unique
	private ServerPlayerEntity soullink$player;

	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	private void update(ServerPlayerEntity player, CallbackInfo ci) {
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		ci.cancel();
	}

	@Inject(method = "addInternal", at = @At("HEAD"), cancellable = true)
	private void eat(int nutrition, float saturation, CallbackInfo ci) {
		ServerPlayerEntity player = this.soullink$getPlayer();
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("eat {} nutrition and {} saturation for player {}", nutrition, saturation, player);

		poolManager.dirtyTracker.markDirty(player.getUuid());
		poolManager.addFood(nutrition, saturation);

		ci.cancel();
	}

	@Override
	public ServerPlayerEntity soullink$getPlayer() {
		return soullink$player;
	}

	@Override
	public void soullink$setPlayer(ServerPlayerEntity player) {
		this.soullink$player = player;
	}

	@Override
	public float soullink$getExhaustion() {
		return this.exhaustion;
	}

	@Override
	public void soullink$setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}

	@Override
	public int soullink$getFoodTickTimer() {
		return this.foodTickTimer;
	}

	@Override
	public void soullink$setFoodTickTimer(int foodTickTimer) {
		this.foodTickTimer = foodTickTimer;
	}
}
