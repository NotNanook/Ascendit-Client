package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.event.EventTarget;
import me.ascendit.event.events.Render2DEvent;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.GuiPlayerInventory;
import me.ascendit.util.HotbarItems;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CommandInvsee extends Command {
	private HotbarItems playerHotbar;
	private GuiInventory screenToOpen;

	public CommandInvsee() {
		super("invsee", ".invsee <player>");

		playerHotbar = null;
	}

	@Override
	public void onCommand(String[] args) {
		toggle();

		if (ModuleManager.invtracker.isToggled()) {
			// get the target player
			EntityPlayer targetPlayer = null;

			for (EntityPlayer player : mc.theWorld.playerEntities) {
				if (player.getName().equalsIgnoreCase(args[1])) {
					targetPlayer = player;
					break;
				}
			}

			if (targetPlayer == null) {
				MinecraftUtils.sendMessage("It seems that the player \"\2478" + args[1] + "\2477\" does not exist or hes not in your render distance");
				return;
			}

			// get the players tracked items
			for (String playerName : ModuleManager.invtracker.getPlayerList()) {
				if (playerName == targetPlayer.getName()) {
					// found player so get tracked hotbar
					for (HotbarItems hotbar : ModuleManager.invtracker.getPlayerHotbars()) {
						if (hotbar.getPlayerName() == targetPlayer.getName()) {
							// link hotbar
							playerHotbar = hotbar;
							break;
						}
					}
				}
			}

			// check if hotbar is valid
			if (playerHotbar == null) {
				MinecraftUtils.sendMessage("An error occured. Please try again");
				return;
			}

			// create fake player
			EntityOtherPlayerMP targetPlayerCopy = new EntityOtherPlayerMP(mc.theWorld, targetPlayer.getGameProfile());
			targetPlayerCopy.clonePlayer(targetPlayer, true);

			// get his inv
			GuiPlayerInventory gui = new GuiPlayerInventory(targetPlayerCopy);
			ItemStack[] itemStack = playerHotbar.getHotbarItems();

			gui.inventorySlots.putStackInSlot(44, itemStack[HotbarItems.STRENGTH_SLOT]);
			gui.inventorySlots.putStackInSlot(43, itemStack[HotbarItems.SHEEP_SLOT]);
			gui.inventorySlots.putStackInSlot(42, itemStack[HotbarItems.BLAZEROD_SLOT]);
			gui.inventorySlots.putStackInSlot(41, itemStack[HotbarItems.TNT_SLOT]);
			gui.inventorySlots.putStackInSlot(40, itemStack[HotbarItems.WATER_SLOT]);
			gui.inventorySlots.putStackInSlot(39, itemStack[HotbarItems.LAVA_SLOT]);
			gui.inventorySlots.putStackInSlot(38, itemStack[HotbarItems.ENDERPEARL_SLOT]);
			gui.inventorySlots.putStackInSlot(37, itemStack[HotbarItems.BOW_SLOT]);
			gui.inventorySlots.putStackInSlot(36, itemStack[HotbarItems.SWORD_SLOT]);

			setGui(gui);
		} else {
			// get the target player
			EntityPlayer targetPlayer = null;

			for (EntityPlayer player : mc.theWorld.playerEntities) {
				if (player.getName().equalsIgnoreCase(args[1])) {
					targetPlayer = player;
					break;
				}
			}

			if (targetPlayer == null) {
				MinecraftUtils.sendMessage("It seems that the player \"\2478" + args[1] + "\2477\" is not in your render distance");
				return;
			}

			GuiPlayerInventory gui = new GuiPlayerInventory(targetPlayer);
			setGui(gui);
		}
	}

	@EventTarget
	public void onRender2D(final Render2DEvent event) {
		if (screenToOpen != null && mc.currentScreen == null) {
			mc.displayGuiScreen(screenToOpen);
			setGui(null);
		} else {
			MinecraftUtils.sendMessage("An error occured while trying to display the Gui");
			System.out.println("Screen to open: " + screenToOpen + " Current Screen: " + mc.currentScreen);
		}

		toggle();
	}

	public void setGui(GuiInventory gui) {
		screenToOpen = gui;
	}
}
