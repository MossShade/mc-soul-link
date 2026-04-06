package com.mossshade.soullink.mixin;

import com.mojang.authlib.GameProfile;
import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Level level, GameProfile gameProfile, CallbackInfo ci) {
		Player player = (Player)(Object) this;
		if (!(player instanceof ServerPlayer serverPlayerEntity) || player instanceof PoolMockPlayer) return;

		FoodData hungerManager = player.getFoodData();

		((HungerManagerAccess) hungerManager).soullink$setPlayer(serverPlayerEntity);
	}

	@Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setHealth(F)V", shift = At.Shift.AFTER))
	public void actuallyHurt(ServerLevel level, DamageSource source, float dmg, CallbackInfo ci) {
		Player self = (Player)(Object) this;
		if (!(self instanceof ServerPlayer player)) return;
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("applyDamage {} for player {}", dmg, player);

		poolManager.dirtyTracker.markDirty(player.getUUID());
		poolManager.addDamage(dmg);
	}

	@Inject(method = "causeFoodExhaustion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V", shift = At.Shift.AFTER))
	public void causeFoodExhaustion(float amount, CallbackInfo ci) {
		Player self = (Player)(Object) this;
		if (!(self instanceof ServerPlayer player)) return;
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("addExhaustion {} for player {}", amount, player);

		poolManager.dirtyTracker.markDirty(player.getUUID());
		poolManager.addExhaustion(amount);
	}

}
