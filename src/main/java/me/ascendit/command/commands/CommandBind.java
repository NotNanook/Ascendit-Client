package me.ascendit.command.commands;

import org.lwjgl.input.Keyboard;

import me.ascendit.command.Command;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;

public class CommandBind extends Command {
    
	public CommandBind() {
        super("Bind", ".bind <module> <key>");
    }

    @Override
	public void onCommand(final String[] args) {
    	for (final Module module : ModuleManager.modules) {
        	if (args.length <= 2 || args == null) {
        		printSyntax();
        		return;
        	}
        	if(args[2].equalsIgnoreCase("none")) {
        		if (module.getName().equalsIgnoreCase(args[1])) {
        			module.setKeybind(999);
        			MinecraftUtils.sendMessage(args[1].toUpperCase() + " removed");
        		}
        	} else if (module.getName().equalsIgnoreCase(args[1])) {
        		if (!Keyboard.getKeyName(Keyboard.getKeyIndex(args[2].toUpperCase())).equalsIgnoreCase("none")) {
        			module.setKeybind(Keyboard.getKeyIndex(args[2].toUpperCase()));
        			MinecraftUtils.sendMessage("\2478" + module.getName() + "\2477 bound to \2478" + args[2].toUpperCase());
        		} else {
        			MinecraftUtils.sendMessage("This is not a valid key");
        		}
        	}
        }
    }
}