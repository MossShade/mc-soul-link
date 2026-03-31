package com.mossshade.soullink.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class Status implements Command<CommandSourceStack> {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
		root.then(Commands.literal(Constants.COMMAND_STATUS).executes(new Status()));
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack serverCommandSource = context.getSource();
		MinecraftServer minecraftServer = serverCommandSource.getServer();

		SharedPoolManager poolManager = PoolAPI.get(minecraftServer);

		serverCommandSource.sendSuccess(() -> Config.getFeedback(ConfigManager.CONFIG, false), false);
		serverCommandSource.sendSuccess(() -> Component.translatable(
				Constants.COMMAND_STATUS_MESSAGE,
				poolManager.getPoolHealth(),
				poolManager.getPoolFoodLevel(),
				poolManager.getPoolSaturationLevel(),
				poolManager.getPoolExhaustion(),
				poolManager.getPoolFoodTickTimer()
		).withStyle(ChatFormatting.ITALIC), false);

		return Command.SINGLE_SUCCESS;
	}

}
