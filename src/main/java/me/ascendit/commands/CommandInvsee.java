package me.ascendit.commands;

import me.ascendit.Main;
import me.ascendit.utils.GuiPlayerInventory;
import me.ascendit.utils.HotbarItems;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class CommandInvsee extends Command
{	
	private HotbarItems playerHotbar;

	public CommandInvsee()
	{
		super("invsee", "Shows you the inventory of a player. Can be combined with InventoryTracker", "Usage: .invsee <player>");
		this.registerCommand();
		
		this.playerHotbar = null;
	}

	@Override
	public void onCommand(String[] args) 
	{
		// get the target player
		EntityPlayer targetPlayer = null;
		
		for(EntityPlayer player : mc.theWorld.playerEntities)
		{
			if(player.getName().equalsIgnoreCase(args[1]))
			{
				targetPlayer = player;
				break;
			}
		}

		if(targetPlayer == null)
		{
			this.sendMessage("It seems that the player " + args[1] + " does not exist", EnumChatFormatting.RED);
			return;
		}

		// get the players tracked items
		for(String playerName : Main.invTracker.getPlayerList())
		{
			if(playerName == targetPlayer.getName())
			{
				// found player so get tracked hotbar
				for(HotbarItems hotbar : Main.invTracker.getPlayerHotbars())
				{
					if(hotbar.getPlayerName() == targetPlayer.getName())
					{
						// link hotbar
						this.playerHotbar = hotbar;
						break;
					}
				}
			}
		}

		// check if hotbar is valid
		if(this.playerHotbar == null)
		{
			this.sendMessage("An error occured. Please try again", EnumChatFormatting.RED);
			return;
		}

		// create fake player
		EntityOtherPlayerMP targetPlayerCopy = new EntityOtherPlayerMP(mc.theWorld, targetPlayer.getGameProfile());
		targetPlayerCopy.clonePlayer(targetPlayer, true);

		// get his inv
		GuiPlayerInventory gui = new GuiPlayerInventory(targetPlayerCopy);
		ItemStack[] itemStack = this.playerHotbar.getHotbarItems();
		
		gui.inventorySlots.putStackInSlot(44, itemStack[HotbarItems.STRENGTH_SLOT]);
		gui.inventorySlots.putStackInSlot(43, itemStack[HotbarItems.SHEEP_SLOT]);
		gui.inventorySlots.putStackInSlot(42, itemStack[HotbarItems.BLAZEROD_SLOT]);
		gui.inventorySlots.putStackInSlot(41, itemStack[HotbarItems.TNT_SLOT]);
		gui.inventorySlots.putStackInSlot(40, itemStack[HotbarItems.WATER_SLOT]);
		gui.inventorySlots.putStackInSlot(39, itemStack[HotbarItems.LAVA_SLOT]);
		gui.inventorySlots.putStackInSlot(38, itemStack[HotbarItems.ENDERPEARL_SLOT]);
		gui.inventorySlots.putStackInSlot(37, itemStack[HotbarItems.BOW_SLOT]);
		gui.inventorySlots.putStackInSlot(36, itemStack[HotbarItems.SWORD_SLOT]);

		Main.invTracker.setGui(gui);
	}
}
