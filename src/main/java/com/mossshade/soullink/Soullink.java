package com.mossshade.soullink;

import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.events.CommandHandler;
import com.mossshade.soullink.events.PlayerRespawnHandler;
import com.mossshade.soullink.events.ServerLifeCycleHandler;
import com.mossshade.soullink.events.ServerTickHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Soullink implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

	@Override
	public void onInitialize() {
		ConfigManager.load();

		ServerLifecycleEvents.SERVER_STARTED.register(ServerLifeCycleHandler::registerStarted);
		ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifeCycleHandler::registerStopped);
		CommandRegistrationCallback.EVENT.register(CommandHandler::register);
		ServerTickEvents.END_WORLD_TICK.register(ServerTickHandler::register);
		ServerPlayerEvents.AFTER_RESPAWN.register(PlayerRespawnHandler::register);

		LOGGER.info(Constants.MOD_ID + " has been initialized.");
	}

}
