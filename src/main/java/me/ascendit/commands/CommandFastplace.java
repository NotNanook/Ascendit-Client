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
        	mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + this.getSyntax()));
            return;
        } 
        else 
        {
            try
            {
                int newDelay = Integer.parseInt(args[1]);
                Main.fastplace.setDelay(newDelay);
                mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "Fastplace" + EnumChatFormatting.WHITE + "]: Delay set to " + args[1]));

            } 
            catch (NumberFormatException e) 
            {
            	mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + this.getSyntax()));
                return;
            }
        }
    }
}