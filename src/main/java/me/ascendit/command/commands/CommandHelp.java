package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.command.CommandManager;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;

public class CommandHelp extends Command {
    
	public CommandHelp() {
        super("Help", ".help <command>/<module>/modules");
    }

    @Override
    public void onCommand(final String[] args) {
    	if (args.length > 2 || args == null) {
    		printSyntax();
    		return;
    	}
        if (args.length == 1) {
        	MinecraftUtils.sendMessage("Commands: ");
            for (final Command command: CommandManager.commands) {
                MinecraftUtils.sendMessagewithoutPrefix("   " + command.getCommand() + ": " + command.getSyntax());
            }
        } else if (args.length == 2) {
        	if (args[1].equalsIgnoreCase("modules")) {
        		MinecraftUtils.sendMessage("Modules: ");
        		for (final Module module: ModuleManager.modules) {
        			MinecraftUtils.sendMessagewithoutPrefix("   " + module.getName() + ": " + module.getDescription());
        		}
        	}
            for (final Command command: CommandManager.commands) {
                if (command.getCommand().equalsIgnoreCase(args[1])) {
                	MinecraftUtils.sendMessage(command.getCommand() + ": " + command.getSyntax());
                }
            }
            for (final Module module: ModuleManager.modules) {
            	if (module.getName().equalsIgnoreCase(args[1])) {
            		MinecraftUtils.sendMessage(module.getName() + ": " +  module.getDescription());
            	}
            }
        }
    }
}