package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;

public class CommandToggle extends Command {
	
    public CommandToggle() {
        super("Toggle", ".t <module>", "t");
    }

    @Override
	public void onCommand(final String[] args) {
    	if (args.length != 2) {
    		printSyntax();
    		return;
    	}
        for (final Module module: ModuleManager.modules) {
            if (module.getName().equalsIgnoreCase(args[1])) {
                module.toggle();
                MinecraftUtils.sendMessage((module.isToggled() ? "Enabled" : "Disabled") + " module \2478" + module.getName());
                return;
            }
        }
        MinecraftUtils.sendMessage("Could not find module: \"\2478" + args[1] + "\2477\"");
    }
}