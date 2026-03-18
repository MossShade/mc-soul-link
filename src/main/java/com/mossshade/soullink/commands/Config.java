package com.mossshade.soullink.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.commands.config.Enable;
import com.mossshade.soullink.commands.config.Reload;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.config.ModConfig;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Config implements Command<ServerCommandSource> {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> root) {
		LiteralArgumentBuilder<ServerCommandSource> config = CommandManager.literal(Constants.COMMAND_CONFIG);

		config.requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS));

		Enable.register(config);
		Reload.register(config);

		root.then(config.executes(new Config()));
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource serverCommandSource = context.getSource();

		serverCommandSource.sendFeedback(() -> getFeedback(ConfigManager.CONFIG, true), false);

		return Command.SINGLE_SUCCESS;
	}

	public static MutableText getEnableStatusMessage(boolean enabled) {
		return Text.translatable(enabled ? Constants.CONFIG_STATUS_ENABLED_MESSAGE : Constants.CONFIG_STATUS_DISABLED_MESSAGE);
	}

	private static ClickEvent getCommandSuggestion(String command, Boolean state) {
		return new ClickEvent.SuggestCommand("/" + Constants.COMMAND_NAME + " " + Constants.COMMAND_CONFIG + " " + command + " " + state);
	}

	public static MutableText getFeedback(ModConfig config, boolean interactable) {
		String padding = "    ";
		MutableText newLine = Text.literal("\n");
		Text hoverMessage = Text.translatable(Constants.COMMAND_HELP_HOVER_MESSAGE);

		MutableText title = Text.translatable(Constants.COMMAND_HELP_CONFIG_TITLE).formatted(Formatting.BOLD);

		MutableText enabled = Text.literal(padding)
				.append(Text.translatable(Constants.COMMAND_HELP_CONFIG_ENABLE)
						.styled(style -> style
								.withColor(config.enabled ? Formatting.GREEN : Formatting.RED)
								.withClickEvent(interactable ? getCommandSuggestion(Constants.COMMAND_CONFIG_ENABLE, !config.enabled) : null)
								.withHoverEvent(interactable ? new HoverEvent.ShowText(hoverMessage) : null)
						)
				);


		return Text.empty()
				.append(title).append(newLine)
				.append(enabled);
	}

}
