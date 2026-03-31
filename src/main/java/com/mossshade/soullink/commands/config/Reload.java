package com.mossshade.soullink.commands.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.config.ConfigManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Reload implements Command<CommandSourceStack> {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
		root.then(Commands.literal(Constants.COMMAND_CONFIG_RELOAD).executes(new Reload()));
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack serverCommandSource = context.getSource();

		ConfigManager.load();

		serverCommandSource.sendSuccess(() -> Component.translatable(Constants.CONFIG_RELOAD_MESSAGE), false);

		return Command.SINGLE_SUCCESS;
	}

}
