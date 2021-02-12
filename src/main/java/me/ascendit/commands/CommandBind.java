package me.ascendit.commands;

import org.lwjgl.input.Keyboard;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandBind extends Command{
	
	public CommandBind()
	{
		super("Bind", "Binds a key to a certain module", "Usage: .bind <module> <key>");
		this.registerCommand();
	}
	
	@Override
    public void onCommand(String[] args)
    {
		if(args.length == 3)
		{
			for(Module module : Main.modules)
	        {
	            if(module.getName().equalsIgnoreCase(args[1])) 
	            {
	                module.setKeyBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
	                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Bound Module " + EnumChatFormatting.WHITE + module.getName() + EnumChatFormatting.GREEN + " to " + EnumChatFormatting.WHITE + args[2]));
	                return;
	            }
	        }
		}
		else
		{
			mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + this.getSyntax()));
            return;
		}
    }
}
