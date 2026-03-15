package com.mossshade.soullink.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.config.ConfigManager;
import com.mossshade.soullink.pool.PoolManager;
import com.mossshade.soullink.pool.SharedPoolState;
import com.mossshade.soullink.utils.Constants;
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

		SharedPoolState pool = PoolManager.getPool(minecraftServer);

		serverCommandSource.sendFeedback(() -> Config.getFeedback(ConfigManager.CONFIG, false), false);
		serverCommandSource.sendFeedback(() -> Text.translatable(Constants.COMMAND_STATUS_MESSAGE, pool.getHealth(), pool.getFoodLevel()).formatted(Formatting.ITALIC), false);

		return Command.SINGLE_SUCCESS;
	}

}
