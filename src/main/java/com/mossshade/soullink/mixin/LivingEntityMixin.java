package com.mossshade.soullink.mixin;

import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.utils.Helpers;
import com.mossshade.soullink.pool.PoolManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
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
		if (player.getGameProfile() == null) return;
		if (ConfigManager.isHealthDisabled()) return;

		Soullink.LOGGER.debug("heal {} for player {}", amount, player);

		PoolManager.setDirtyPlayer(player);
		PoolManager.propagateHeal(Helpers.getMinecraftServer(player), amount);
	}

	@Inject(method = "tryUseDeathProtector", at = @At("TAIL"))
	public void tryUseDeathProtector(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity livingEntity = (LivingEntity)(Object) this;
		if (!(livingEntity instanceof ServerPlayerEntity player)) return;
		if (player.getGameProfile() == null) return;
		if (ConfigManager.isHealthDisabled()) return;

		Boolean usedDeathProtector = cir.getReturnValue();

		Soullink.LOGGER.debug("tryUseDeathProtector {} for player {}", usedDeathProtector, player);

		MinecraftServer server = Helpers.getMinecraftServer(player);

		PoolManager.setDirtyPlayer(player);
		if (usedDeathProtector) {
			PoolManager.propagateHealth(server, player.getHealth());
		} else if (player.isDead()) {
			PoolManager.killEveryone(server, source);
		}
	}

}
