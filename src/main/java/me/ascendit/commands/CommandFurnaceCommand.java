package me.ascendit.commands;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandFurnaceCommand extends Command {
		
	public CommandFurnaceCommand() {
		super("furnacecommand", "Gives you a furnace wtih spawners that generate Command Blocks", "Usage: .furnacecommand <command>");
		this.registerCommand();
	}

	@Override
	public void onCommand(String[] args) {
		if(mc.thePlayer.capabilities.isCreativeMode) {
			String command = "";
			for (int i = 1; i < args.length; i++) {
				command += args[i] + " ";			
			}
			mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, this.addItemtoFurnace(this.generateOPItem(command))));
			this.sendMessage("You got the item", EnumChatFormatting.GREEN);
		} else {
			this.sendMessage("You have to be in creative mode", EnumChatFormatting.RED);
			return;
		}
	}
	
	public ItemStack generateOPItem(String command) {
		ItemStack itm = new ItemStack(Blocks.mob_spawner);
		NBTTagCompound base = new NBTTagCompound();
		NBTTagCompound blockEntityTag = new NBTTagCompound();
		
		blockEntityTag.setString("EntityId", "FallingSand");
		blockEntityTag.setInteger("SpawnCount", 1);
		blockEntityTag.setInteger("SpawnRange", 5);
		
		blockEntityTag.setInteger("RequiredPlayerRange", 100);
		blockEntityTag.setInteger("MinSpawnDelay", 20);
		blockEntityTag.setInteger("MaxSpawnDelay", 20);
		
		blockEntityTag.setInteger("MaxNearbyEntities", 100);
		
		NBTTagCompound spawnData = new NBTTagCompound();
		spawnData.setString("Tile", "minecraft:command_block");
		spawnData.setString("Block", "minecraft:command_block");
		spawnData.setInteger("Time", 1);
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Command", command);
		spawnData.setTag("TileEntityData", tag);
		
		blockEntityTag.setTag("SpawnData", spawnData);
		base.setTag("BlockEntityTag", blockEntityTag);
		itm.setTagCompound(base);
 		return itm;
		
	}
	
	public ItemStack addItemtoFurnace(ItemStack itm) {
		ItemStack furnace = new ItemStack(Blocks.furnace);
		NBTTagCompound base = new NBTTagCompound();
		NBTTagCompound blockEntityTag = new NBTTagCompound();
		
		blockEntityTag.setShort("BurnTime", (short) 0);
		blockEntityTag.setShort("CookTime", (short) 0);
		blockEntityTag.setShort("CookTimeTotal", (short) 200);
		
		blockEntityTag.setString("id", "Furnace");
		blockEntityTag.setString("Lock", "");
		
		NBTTagList items = new NBTTagList();
		NBTTagCompound item = new NBTTagCompound();
		item.setByte("Count", (byte) 1);
		item.setShort("Damage", (short) itm.getItemDamage());
		item.setString("id", "minecraft:mob_spawner");
		item.setShort("Slot", (short) 0);
		
		item.setTag("tag", itm.getTagCompound());
		
		items.appendTag(item);
		
		blockEntityTag.setTag("Items", items);
		base.setTag("BlockEntityTag", blockEntityTag);
		
		furnace.setTagCompound(base);
		return furnace;
	}
}