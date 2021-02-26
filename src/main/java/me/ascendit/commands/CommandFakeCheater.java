package me.ascendit.commands;

import me.ascendit.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandFakeCheater extends Command
{
	public CommandFakeCheater()
	{
		super("FakeCheater", "Makes it look like a person is cheating", "Usage: .fakecheater <add/remove/clear> <name>");
		this.registerCommand();
	}
	
	@Override
	public void onCommand(String[] args) 
	{
		if(args.length == 2)
		{
			if(args[1].equalsIgnoreCase("clear"))
			{
				// clear list
				Main.fakeCheater.getPlayerList().clear();
				mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Cleared fake cheater list"));
				return;
			}
		}
		else if(args.length == 3)
		{
			if(args[1].equalsIgnoreCase("add"))
			{
				// add player to list
				for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
				{
					if(entity instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) entity;
						if(player.getName().equalsIgnoreCase(args[2]) && !Main.fakeCheater.getPlayerList().contains(player))
						{
							Main.fakeCheater.getPlayerList().add(player);
							mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Added player " + player.getName()));
							return;
						}
					}
				}
			}
			else if(args[1].equalsIgnoreCase("remove"))
			{
				// remove player from list
				for(EntityPlayer player : Main.fakeCheater.getPlayerList())
				{
					if(player.getName().equalsIgnoreCase(args[2]) && Main.fakeCheater.getPlayerList().contains(player))
					{
						Main.fakeCheater.getPlayerList().remove(player);
						mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Removed player " + player.getName()));
						return;
					}
				}
			}
			else
			{
				mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + this.getSyntax()));
			}
		}
		else
		{
			mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + this.getSyntax()));
            return;
		}
	}
}
