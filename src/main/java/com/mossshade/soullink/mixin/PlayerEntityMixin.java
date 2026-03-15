package com.mossshade.soullink.mixin;

import com.mojang.authlib.GameProfile;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.utils.Helpers;
import com.mossshade.soullink.pool.PoolManager;
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
		if (!(player instanceof ServerPlayerEntity serverPlayerEntity)) return;

		HungerManager hungerManager = player.getHungerManager();

		((HungerManagerAccess) hungerManager).soullink$setPlayer(serverPlayerEntity);
	}

	@Inject(method = "applyDamage", at = @At("TAIL"))
	public void applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object) this;
		if (!(self instanceof ServerPlayerEntity player)) return;
		if (player == null || player.getGameProfile() == null) return;
		if (ConfigManager.isHealthDisabled()) return;

		Soullink.LOGGER.debug("applyDamage {} for player {}", amount, player);

		PoolManager.setDirtyPlayer(player);
		PoolManager.propagateDamage(Helpers.getMinecraftServer(player), amount);
	}

}
