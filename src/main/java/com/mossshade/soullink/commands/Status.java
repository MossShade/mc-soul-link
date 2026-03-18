package com.mossshade.soullink.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.pool.PoolAPI;
import com.mossshade.soullink.pool.SharedPoolManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Status implements Command<ServerCommandSource> {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> root) {
		root.then(CommandManager.literal(Constants.COMMAND_STATUS).executes(new Status()));
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource serverCommandSource = context.getSource();
		MinecraftServer minecraftServer = serverCommandSource.getServer();

		SharedPoolManager poolManager = PoolAPI.get(minecraftServer);

		serverCommandSource.sendFeedback(() -> Config.getFeedback(ConfigManager.CONFIG, false), false);
		serverCommandSource.sendFeedback(() -> Text.translatable(
				Constants.COMMAND_STATUS_MESSAGE,
				poolManager.getPoolHealth(),
				poolManager.getPoolFoodLevel(),
				poolManager.getPoolSaturationLevel(),
				poolManager.getPoolExhaustion(),
				poolManager.getPoolFoodTickTimer()
		).formatted(Formatting.ITALIC), false);

		return Command.SINGLE_SUCCESS;
	}

}
