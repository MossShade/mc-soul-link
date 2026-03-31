package com.mossshade.soullink.mixin;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.overrides.PoolMockPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {

	@Inject(method = "tickRegeneration", at = @At("HEAD"), cancellable = true)
	public void tickHunger(CallbackInfo ci) {
		ServerPlayer self = (ServerPlayer)(Object) this;
		if (self.getGameProfile() == null || self instanceof PoolMockPlayer) return;
		if (ConfigManager.isDisabled()) return;

		// TODO: Mock this call for peaceful mode
		ci.cancel();
	}

}
