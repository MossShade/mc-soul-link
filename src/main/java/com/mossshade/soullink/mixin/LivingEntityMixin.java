package com.mossshade.soullink.mixin;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	public void heal(float amount, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity)(Object) this;
		if (!(livingEntity instanceof ServerPlayer player)) return;
		if (player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("heal {} for player {}", amount, player);

		poolManager.dirtyTracker.markDirty(player.getUUID());
		poolManager.addHeal(amount);

		ci.cancel();
	}

	@Inject(method = "checkTotemDeathProtection", at = @At("TAIL"))
	public void tryUseDeathProtector(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity livingEntity = (LivingEntity)(Object) this;
		if (!(livingEntity instanceof ServerPlayer player)) return;
		if (player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		Boolean usedDeathProtector = cir.getReturnValue();

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("tryUseDeathProtector {} for player {}", usedDeathProtector, player);

		poolManager.dirtyTracker.markDirty(player.getUUID());
		if (usedDeathProtector) {
			poolManager.propagateHealth(1.0F);
		} else {
			poolManager.killEveryone(source);
		}
	}

}
