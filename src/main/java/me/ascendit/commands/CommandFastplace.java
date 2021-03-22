package me.ascendit.commands;

import me.ascendit.Main;
import me.ascendit.modules.misc.ModuleFastplace;
import net.minecraft.util.ChatComponentText;
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
            try
            {
                int newDelay = Integer.parseInt(args[1]);
                Main.fastplace.setDelay(newDelay);
                this.sendMessage("Delay set to " + EnumChatFormatting.BOLD + args[1], EnumChatFormatting.WHITE);

            } 
            catch (NumberFormatException e) 
            {
            	this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
                return;
            }
        }
    }
}