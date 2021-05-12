package me.ascendit.command.commands;

import org.lwjgl.input.Keyboard;

import me.ascendit.command.Command;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;

public class CommandBinds extends Command {
	
	public CommandBinds() {
		super("Binds", ".binds");
	}
	
	@Override
	public void onCommand(final String[] args) {
		if (args.length == 1) {
			MinecraftUtils.sendMessage("Binds:");
			for (final Module module: ModuleManager.modules) {
				if (module.getKeybind() != 999) {
					MinecraftUtils.sendMessagewithoutPrefix("   " + module.getName() + " -> " + Keyboard.getKeyName(module.getKeybind()));
				}
			}
		} else if (args[1].equalsIgnoreCase("clear")) {
			for (final Module module : ModuleManager.modules) {
				module.setKeybind(999);
			}
			MinecraftUtils.sendMessage("Binds cleared");
		} else {
			printSyntax();
		}
	}
}
