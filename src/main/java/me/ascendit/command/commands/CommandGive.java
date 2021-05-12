package me.ascendit.command.commands;

import org.apache.commons.lang3.StringUtils;

import me.ascendit.command.Command;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandGive extends Command {
	
	public CommandGive() {
		super("Give", ".give <item> <amount> <nbt>", "i", "get", "item");
	}

	@Override
	public void onCommand(final String[] args) {
		if (args.length < 2) {
			printSyntax();
			return;
		}
		if (Item.getByNameOrId(args[1]) != null) {
			if (mc.thePlayer.capabilities.isCreativeMode) {
				if (args.length == 2) {
					final ItemStack itm = new ItemStack(Item.getByNameOrId(args[1]));
					mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
					MinecraftUtils.sendMessage("You got the item \"\2478" + args[1].toLowerCase() + "\2477\"");
					mc.thePlayer.playSound("random.pop", 1F, 1F);
				} else if (args.length == 3) {
					if (StringUtils.isNumeric(args[2])) {
						final ItemStack itm = new ItemStack(Item.getByNameOrId(args[1]), Integer.parseInt(args[2]));
						mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
						MinecraftUtils.sendMessage("You got the item \"\2478" + args[1].toLowerCase() + "\2477\"");
						mc.thePlayer.playSound("random.pop", 1F, 1F);
					} else {
						MinecraftUtils.sendMessage("Please specify the amount");
						return;
					}
				} else if (args.length == 4) {
					if (StringUtils.isNumeric(args[2])) {
						final ItemStack itm = new ItemStack(Item.getByNameOrId(args[1]), Integer.parseInt(args[2]));
						try {
							itm.setTagCompound(JsonToNBT.getTagFromJson(args[3]));
						} catch (final NBTException e) {
							e.printStackTrace();
							MinecraftUtils.sendMessage("That is not a valid NBT-Tag");
						}
						mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
						MinecraftUtils.sendMessage("You got the item \"\2478" + args[1].toLowerCase() + "\2477\"");
						mc.thePlayer.playSound("random.pop", 1F, 1F);
					} else {
						MinecraftUtils.sendMessage("Please specify the amount");
						return;
					}
				}
			} else {
				MinecraftUtils.sendMessage("You have to be in creative mode");
				return;
			}
		} else {
			MinecraftUtils.sendMessage("This item does not exist");
			return;
		}
	}
}
