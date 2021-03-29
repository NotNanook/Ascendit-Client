package me.ascendit.commands;

import me.ascendit.Main;
import net.minecraft.util.EnumChatFormatting;

public class CommandHelp extends Command 
{
	public CommandHelp()
	{
		super("help", "Shows you every command syntax or just for a specific one", "Usage: .help <none/command>");
		this.registerCommand();
	}
	
	@Override
	public void onCommand(String[] args) 
	{
		if(args.length == 1)
		{
			for(Command command : Main.commands)
			{
				this.sendMessage(command.getSyntax(), EnumChatFormatting.YELLOW);
			}
		}
		else if(args.length == 2)
		{
			for(Command command : Main.commands)
			{
				if(command.command.equalsIgnoreCase(args[1]))
				{
					this.sendMessage(command.getSyntax() + " " + command.getDescription(), EnumChatFormatting.YELLOW);
				}
			}
		}
	}
}
