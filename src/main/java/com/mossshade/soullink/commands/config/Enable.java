package com.mossshade.soullink.commands.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.commands.Config;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.utils.Constants;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Enable implements Command<ServerCommandSource> {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> root) {
		root.then(CommandManager.literal(Constants.COMMAND_CONFIG_ENABLE)
				.then(CommandManager.argument(Constants.COMMAND_CONFIG_ARG, BoolArgumentType.bool())
						.executes(new Enable())
				)
		);
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		boolean enabled = BoolArgumentType.getBool(context, Constants.COMMAND_CONFIG_ARG);

		ServerCommandSource serverCommandSource = context.getSource();

		ConfigManager.CONFIG.enabled = enabled;
		ConfigManager.save();

		serverCommandSource.sendFeedback(() -> Text.translatable(Constants.CONFIG_ENABLE_MESSAGE, Config.getEnableStatusMessage(enabled)), false);
		serverCommandSource.sendFeedback(() -> Config.getFeedback(ConfigManager.CONFIG, true), false);

		return Command.SINGLE_SUCCESS;
	}

}
