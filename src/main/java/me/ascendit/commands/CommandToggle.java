package me.ascendit.commands;


import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandToggle extends Command
{
	public CommandToggle()
	{
		super("t", "Toggles a module for you", "Usage: .t <module>");
		this.registerCommand();
	}

	@Override
	public void onCommand(String[] args) 
	{
		if(args.length != 2)
		{
			this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
			return;
		}
		
		for(Module module : Main.modules.moduleList)
		{
			if(module.getName().equalsIgnoreCase(args[1]))
			{
				if(module.isEnabled())
				{
					module.disable();
					return;
				}
				else
				{
					module.enable();
					return;
				}	
			}
		}
		
		this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
	}
}
