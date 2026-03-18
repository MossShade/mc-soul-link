package com.mossshade.soullink;

import com.mossshade.soullink.interfaces.HungerManagerAccess;
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

	public static void setSaturation(MinecraftClient client, Float saturationLevel) {
		assert client.player != null;

		client.player.getHungerManager().setSaturationLevel(saturationLevel);
	}

	public static void setExhaustion(MinecraftClient client, Float exhaustion) {
		assert client.player != null;

		HungerManagerAccess access = (HungerManagerAccess) client.player.getHungerManager();

		access.soullink$setExhaustion(exhaustion);
	}

	public static void setFoodTickTimer(MinecraftClient client, Integer foodTickTimer) {
		assert client.player != null;

		HungerManagerAccess access = (HungerManagerAccess) client.player.getHungerManager();

		access.soullink$setFoodTickTimer(foodTickTimer);
	}
}
