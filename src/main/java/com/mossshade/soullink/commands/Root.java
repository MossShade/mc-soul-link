package com.mossshade.soullink.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mossshade.soullink.Constants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Root {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Constants.COMMAND_NAME);

		Status.register(root);
		Reset.register(root);
		Config.register(root);

		dispatcher.register(root);
	}

}
