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
import com.mossshade.soullink.utils.LocalizedText;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.permissions.Permissions;

public class Config implements Command<CommandSourceStack> {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
		LiteralArgumentBuilder<CommandSourceStack> config = Commands.literal(Constants.COMMAND_CONFIG);

		config.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR));

		Enable.register(config);
		Reload.register(config);

		root.then(config.executes(new Config()));
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack serverCommandSource = context.getSource();

		serverCommandSource.sendSuccess(() -> getFeedback(ConfigManager.CONFIG, true), false);

		return Command.SINGLE_SUCCESS;
	}

	public static MutableComponent getEnableStatusMessage(boolean enabled) {
		return LocalizedText.getTranslatableWithFallback(enabled ? Constants.CONFIG_STATUS_ENABLED_MESSAGE : Constants.CONFIG_STATUS_DISABLED_MESSAGE);
	}

	private static ClickEvent getCommandSuggestion(String command, Boolean state) {
		return new ClickEvent.SuggestCommand("/" + Constants.COMMAND_NAME + " " + Constants.COMMAND_CONFIG + " " + command + " " + state);
	}

	public static MutableComponent getFeedback(ModConfig config, boolean interactable) {
		String padding = "    ";
		MutableComponent newLine = Component.literal("\n");
		Component hoverMessage = LocalizedText.getTranslatableWithFallback(Constants.COMMAND_HELP_HOVER_MESSAGE);

		MutableComponent title = LocalizedText.getTranslatableWithFallback(Constants.COMMAND_HELP_CONFIG_TITLE).withStyle(ChatFormatting.BOLD);

		MutableComponent enabled = Component.literal(padding)
				.append(LocalizedText.getTranslatableWithFallback(Constants.COMMAND_HELP_CONFIG_ENABLE)
						.withStyle(style -> style
								.withColor(config.enabled ? ChatFormatting.GREEN : ChatFormatting.RED)
								.withClickEvent(interactable ? getCommandSuggestion(Constants.COMMAND_CONFIG_ENABLE, !config.enabled) : null)
								.withHoverEvent(interactable ? new HoverEvent.ShowText(hoverMessage) : null)
						)
				);


		return Component.empty()
				.append(title).append(newLine)
				.append(enabled);
	}

}
