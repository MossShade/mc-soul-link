package com.mossshade.soullink.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Reset implements Command<ServerCommandSource> {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> root) {
		root.then(CommandManager.literal(Constants.COMMAND_RESET)
				.requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
				.executes(new Reset())
		);
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource serverCommandSource = context.getSource();
		MinecraftServer minecraftServer = serverCommandSource.getServer();

		PoolAPI.get(minecraftServer).reset();

		serverCommandSource.sendFeedback(() -> Text.translatable(Constants.COMMAND_RESET_MESSAGE), false);

		return Command.SINGLE_SUCCESS;
	}

}
