package com.mossshade.soullink.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mossshade.soullink.Soullink;
import com.mossshade.soullink.utils.Constants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(Constants.CONFIG_FILE);

	public static ModConfig CONFIG = new ModConfig();

	public static void load() {
		try {
			if (Files.exists(CONFIG_PATH)) {
				Reader reader = Files.newBufferedReader(CONFIG_PATH);
				CONFIG = GSON.fromJson(reader, ModConfig.class);
				reader.close();
			} else {
				save();
			}
		} catch (Exception e) {
			Soullink.LOGGER.error("load config error : {}", e.getMessage(), e);
		}
	}

	public static void save() {
		try {
			Writer writer = Files.newBufferedWriter(CONFIG_PATH);
			GSON.toJson(CONFIG, writer);
			writer.close();
		} catch (Exception e) {
			Soullink.LOGGER.error("save config error : {}", e.getMessage(), e);
		}
	}

	public static boolean isHealthDisabled() {
		return !CONFIG.enabled || !CONFIG.shared_health;
	}

	public static boolean isFoodDisabled() {
		return !CONFIG.enabled || !CONFIG.shared_food;
	}

}
