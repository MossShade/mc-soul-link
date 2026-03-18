package com.mossshade.soullink.mixin;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "heal", at = @At("TAIL"))
	public void heal(float amount, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity)(Object) this;
		if (!(livingEntity instanceof ServerPlayerEntity player)) return;
		if (player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("heal {} for player {}", amount, player);

		poolManager.dirtyTracker.markDirty(player.getUuid());
		poolManager.addHeal(amount);
	}

	@Inject(method = "tryUseDeathProtector", at = @At("TAIL"))
	public void tryUseDeathProtector(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity livingEntity = (LivingEntity)(Object) this;
		if (!(livingEntity instanceof ServerPlayerEntity player)) return;
		if (player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		Boolean usedDeathProtector = cir.getReturnValue();

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("tryUseDeathProtector {} for player {}", usedDeathProtector, player);

		poolManager.dirtyTracker.markDirty(player.getUuid());
		if (usedDeathProtector) {
			poolManager.propagateHealth(player.getHealth());
		} else {
			poolManager.killEveryone(source);
		}
	}

}
