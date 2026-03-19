package com.mossshade.soullink.mixin;

import com.mojang.authlib.GameProfile;
import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(World world, GameProfile profile, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity)(Object) this;
		if (!(player instanceof ServerPlayerEntity serverPlayerEntity) || player instanceof PoolMockPlayer) return;

		HungerManager hungerManager = player.getHungerManager();

		((HungerManagerAccess) hungerManager).soullink$setPlayer(serverPlayerEntity);
	}

	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"))
	public void applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object) this;
		if (!(self instanceof ServerPlayerEntity player)) return;
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("applyDamage {} for player {}", amount, player);

		poolManager.dirtyTracker.markDirty(player.getUuid());
		poolManager.addDamage(amount);
	}

	@Inject(method = "addExhaustion", at = @At("TAIL"))
	public void addExhaustion(float exhaustion, CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object) this;
		if (!(self instanceof ServerPlayerEntity player)) return;
		if (player == null || player.getGameProfile() == null || player instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		SharedPoolManager poolManager = PoolAPI.get(player);

		Soullink.LOGGER.debug("addExhaustion {} for player {}", exhaustion, player);

		poolManager.dirtyTracker.markDirty(player.getUuid());
		poolManager.addExhaustion(exhaustion);
	}

}
