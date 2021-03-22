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
			for(Module module : Main.modules.moduleList)
	        {
	            if(module.getName().equalsIgnoreCase(args[1])) 
	            {
	                module.setKeyBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
	                this.sendMessage("Bound module " + module.getName() + " to " + EnumChatFormatting.BOLD + args[2], EnumChatFormatting.WHITE);
	                return;
	            }
	        }
		}
		else
		{
			this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
            return;
		}
    }
}
