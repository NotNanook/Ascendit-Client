package me.ascendit.commands;

import org.apache.commons.lang3.StringUtils;

import me.ascendit.Main;
import net.minecraft.util.EnumChatFormatting;

public class CommandFastplace extends Command {

    public CommandFastplace()
    {
        super("SetDelay", "Set the block place delay for fastplace", "Usage: .setdelay <delay>");
        this.registerCommand();
    }

    @Override
    public void onCommand(String[] args)
    {
        if (args.length != 2)
        {
        	this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
            return;
        } 
        else 
        {
            if(StringUtils.isNumeric(args[1]))
            {
                int newDelay = Integer.parseInt(args[1]);
                Main.fastplace.setDelay(newDelay);
                this.sendMessage("Delay set to " + EnumChatFormatting.BOLD + args[1], EnumChatFormatting.WHITE);

            } 
            else
            {
            	this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
            }
        }
    }
}