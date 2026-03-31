package com.mossshade.soullink.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mossshade.soullink.Constants;
import com.mossshade.soullink.pool.PoolAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permissions;

public class Reset implements Command<CommandSourceStack> {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
		root.then(Commands.literal(Constants.COMMAND_RESET)
				.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
				.executes(new Reset())
		);
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack serverCommandSource = context.getSource();
		MinecraftServer minecraftServer = serverCommandSource.getServer();

		PoolAPI.get(minecraftServer).reset();

		serverCommandSource.sendSuccess(() -> Component.translatable(Constants.COMMAND_RESET_MESSAGE), false);

		return Command.SINGLE_SUCCESS;
	}

}
