package com.mossshade.soullink;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.events.CommandHandler;
import com.mossshade.soullink.events.PlayerJoinHandler;
import com.mossshade.soullink.events.PlayerRespawnHandler;
import com.mossshade.soullink.events.ServerTickHandler;
import com.mossshade.soullink.pool.SharedPoolPayload;
import com.mossshade.soullink.utils.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Soullink implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

	@Override
	public void onInitialize() {
		ConfigManager.load();

		PayloadTypeRegistry.playS2C().register(SharedPoolPayload.ID, SharedPoolPayload.CODEC);

		CommandRegistrationCallback.EVENT.register(CommandHandler::register);
		ServerTickEvents.END_WORLD_TICK.register(ServerTickHandler::register);
		ServerPlayConnectionEvents.JOIN.register(PlayerJoinHandler::register);
		ServerPlayerEvents.AFTER_RESPAWN.register(PlayerRespawnHandler::register);

		LOGGER.info(Constants.MOD_ID + " has been initialized.");
	}

}
