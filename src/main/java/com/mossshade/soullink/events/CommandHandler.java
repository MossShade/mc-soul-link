package com.mossshade.soullink.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mossshade.soullink.commands.Root;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandHandler {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
		Root.register(dispatcher);
	}

}
