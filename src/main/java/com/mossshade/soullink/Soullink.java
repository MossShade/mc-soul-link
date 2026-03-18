package com.mossshade.soullink;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.events.*;
import com.mossshade.soullink.pool.SharedPoolPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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

		ServerLifecycleEvents.SERVER_STARTED.register(ServerLifeCycleHandler::registerStarted);
		ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifeCycleHandler::registerStopped);
		CommandRegistrationCallback.EVENT.register(CommandHandler::register);
		ServerTickEvents.END_WORLD_TICK.register(ServerTickHandler::register);
		ServerPlayConnectionEvents.JOIN.register(PlayerJoinHandler::register);
		ServerPlayerEvents.AFTER_RESPAWN.register(PlayerRespawnHandler::register);

		LOGGER.info(Constants.MOD_ID + " has been initialized.");
	}

}
