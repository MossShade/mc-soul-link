package com.mossshade.soullink;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SoulLinkClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("SoulLink Client Initialized");
	}
}