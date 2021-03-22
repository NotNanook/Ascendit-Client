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
				this.sendMessage("Cleared fakecheater list", EnumChatFormatting.GREEN);
				return;
			}
		}
		if(args.length == 3)
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
							this.sendMessage("Added player " + player.getName(), EnumChatFormatting.GREEN);
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
						this.sendMessage("Removed player " + player.getName(), EnumChatFormatting.GREEN);
						return;
					}
				}
				this.sendMessage("It seems that the player " + args[2] + " doesnt exist", EnumChatFormatting.RED);
			}
			else
			{
				this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
			}
		}
		else
		{
			this.sendMessage(this.getSyntax(), EnumChatFormatting.YELLOW);
            return;
		}
	}
}
