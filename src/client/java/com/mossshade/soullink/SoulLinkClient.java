package com.mossshade.soullink;

import com.mossshade.soullink.pool.SharedPoolPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SoulLinkClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SharedPoolPayload.ID, (payload, context) -> {
			LOGGER.info("Received Shared Pool Payload : {}", payload);
			MinecraftClient client = MinecraftClient.getInstance();
			client.execute(() -> {
				if (client.player instanceof ClientPlayerEntity) {
					if (client.player.getUuid().toString().equals(payload.playerUuid())) return;

					ClientSharedPool.setHealth(client, payload.health());
					ClientSharedPool.setFood(client, payload.foodLevel());
					ClientSharedPool.setSaturation(client, payload.saturationLevel());
					ClientSharedPool.setExhaustion(client, payload.exhaustion());
					ClientSharedPool.setFoodTickTimer(client, payload.foodTickTimer());
				}
			});
		});

		LOGGER.info("SoulLink Client Initialized");
	}
}