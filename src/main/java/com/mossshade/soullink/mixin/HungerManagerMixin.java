package com.mossshade.soullink.mixin;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.interfaces.HungerManagerAccess;
import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.utils.Helpers;
import com.mossshade.soullink.pool.PoolManager;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements HungerManagerAccess {

	@Unique
	private ServerPlayerEntity soullink$player;

	// TODO: Look at changing this to an Injection
	@Redirect(method = "update", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/HungerManager;foodLevel:I", opcode = Opcodes.PUTFIELD))
	private void addExhaustion(HungerManager instance, int value) {
		instance.setFoodLevel(value);

		ServerPlayerEntity player = this.soullink$getPlayer();
		if (player == null || player.getGameProfile() == null) return;
		if (ConfigManager.isFoodDisabled()) return;

		Soullink.LOGGER.debug("update foodLevel {} for player {}", value, player);

		PoolManager.setDirtyPlayer(player);
		PoolManager.propagateFood(Helpers.getMinecraftServer(player), (float) value);
	}

	@Inject(method = "eat", at = @At("TAIL"))
	private void eat(FoodComponent foodComponent, CallbackInfo ci) {
		ServerPlayerEntity player = this.soullink$getPlayer();
		if (player == null || player.getGameProfile() == null) return;
		if (ConfigManager.isFoodDisabled()) return;

		Soullink.LOGGER.debug("eat {} nutrition and {} saturation for player {}", foodComponent.nutrition(), foodComponent.saturation(), player);

		PoolManager.setDirtyPlayer(player);
		PoolManager.propagateEating(Helpers.getMinecraftServer(player), foodComponent.nutrition());
	}

	@Override
	public ServerPlayerEntity soullink$getPlayer() {
		return soullink$player;
	}

	@Override
	public void soullink$setPlayer(ServerPlayerEntity player) {
		this.soullink$player = player;
	}
}
