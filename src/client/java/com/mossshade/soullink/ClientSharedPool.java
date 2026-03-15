package com.mossshade.soullink;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ClientSharedPool {

	public static void setHealth(MinecraftClient client, Float health) {
		assert client.player != null;
		client.player.setHealth(health);
	}

	public static void setFood(MinecraftClient client, Integer food) {
		assert client.player != null;
		client.player.getHungerManager().setFoodLevel(food);
	}

}
