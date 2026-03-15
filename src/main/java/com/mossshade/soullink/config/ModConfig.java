package com.mossshade.soullink.config;

public class ModConfig {

	public boolean enabled;

	public boolean shared_health;

	public boolean shared_food;

	@Override
	public String toString() {
		return "ModConfig{" +
				"enabled=" + enabled +
				", shared_health=" + shared_health +
				", shared_food=" + shared_food +
				'}';
	}

}
