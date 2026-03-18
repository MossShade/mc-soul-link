package com.mossshade.soullink.commands.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.config.ConfigManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Reload implements Command<ServerCommandSource> {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> root) {
		root.then(CommandManager.literal(Constants.COMMAND_CONFIG_RELOAD).executes(new Reload()));
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource serverCommandSource = context.getSource();

		ConfigManager.load();

		serverCommandSource.sendFeedback(() -> Text.translatable(Constants.CONFIG_RELOAD_MESSAGE), false);

		return Command.SINGLE_SUCCESS;
	}

}
