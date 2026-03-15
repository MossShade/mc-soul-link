package com.mossshade.soullink.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mossshade.soullink.commands.Root;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandHandler {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		Root.register(dispatcher);
	}

}
