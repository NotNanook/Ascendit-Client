package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.util.MinecraftUtils;
import me.ascendit.util.StringUtils;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandFurnaceCommand extends Command {
	
	public CommandFurnaceCommand() {
		super("FurnaceCommand", ".furnacecommand <command>");
	}

	@Override
	public void onCommand(final String[] args) {
		if(mc.thePlayer.capabilities.isCreativeMode) {
			mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, addItemtoFurnace(generateCommandItem(StringUtils.toCompleteString(args, 1)))));
			MinecraftUtils.sendMessage("You got an Item");
			mc.thePlayer.playSound("random.pop", 0.5F, 1F);
		} else {
			MinecraftUtils.sendMessage("You have to be in creative mode");
			mc.thePlayer.playSound("random.anvil_land", 0.5F, 1F);
			return;
		}
	}
	
	public ItemStack generateCommandItem(final String command) {
		final ItemStack itm = new ItemStack(Blocks.mob_spawner);
		final NBTTagCompound base = new NBTTagCompound();
		final NBTTagCompound blockEntityTag = new NBTTagCompound();
		
		blockEntityTag.setString("EntityId", "FallingSand");
		blockEntityTag.setInteger("SpawnCount", 1);
		blockEntityTag.setInteger("SpawnRange", 5);
		
		blockEntityTag.setInteger("RequiredPlayerRange", 100);
		blockEntityTag.setInteger("MinSpawnDelay", 20);
		blockEntityTag.setInteger("MaxSpawnDelay", 20);
		
		blockEntityTag.setInteger("MaxNearbyEntities", 100);
		
		final NBTTagCompound spawnData = new NBTTagCompound();
		spawnData.setString("Tile", "minecraft:command_block");
		spawnData.setString("Block", "minecraft:command_block");
		spawnData.setInteger("Time", 1);
		
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Command", command);
		spawnData.setTag("TileEntityData", tag);
		
		blockEntityTag.setTag("SpawnData", spawnData);
		base.setTag("BlockEntityTag", blockEntityTag);
		itm.setTagCompound(base);
 		return itm;
		
	}
	
	public ItemStack addItemtoFurnace(final ItemStack itm) {
		final ItemStack furnace = new ItemStack(Blocks.furnace);
		final NBTTagCompound base = new NBTTagCompound();
		final NBTTagCompound blockEntityTag = new NBTTagCompound();
		
		blockEntityTag.setShort("BurnTime", (short) 0);
		blockEntityTag.setShort("CookTime", (short) 0);
		blockEntityTag.setShort("CookTimeTotal", (short) 200);
		
		blockEntityTag.setString("id", "Furnace");
		blockEntityTag.setString("Lock", "");
		
		final NBTTagList items = new NBTTagList();
		final NBTTagCompound item = new NBTTagCompound();
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