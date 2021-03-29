package me.ascendit.commands;

import me.ascendit.Main;
import net.minecraft.util.EnumChatFormatting;

public class CommandAutoclicker extends Command
{
	public CommandAutoclicker()
	{
		super("Autoclicker", "Sets your autoclicker mode", "Usage: .autoclicker <left/right/both>");
		this.registerCommand();
	}

	@Override
	public void onCommand(String[] args) 
	{
		if(args[1].equalsIgnoreCase("left") || args[1].equalsIgnoreCase("right") || args[1].equalsIgnoreCase("both"))
		{
			Main.autoclicker.setMode(args[1]);
			this.sendMessage("Set autoclicker to mode " + args[1], EnumChatFormatting.GREEN);
		}
		else
		{
			this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
		}
	}
}
