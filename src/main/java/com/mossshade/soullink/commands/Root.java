package com.mossshade.soullink.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mossshade.soullink.utils.Constants;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Root {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal(Constants.COMMAND_NAME);

		Status.register(root);
		Reset.register(root);
		Config.register(root);

		dispatcher.register(root);
	}

}
