package me.ascendit.modules.misc;

import org.lwjgl.input.Keyboard;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemCloth;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModuleEasyHiveBed extends Module{
	
	protected int radius = 3;
	protected int counter; // makes sure that it only places blocks every 5 ticks (otherwise it wont work)
	
	public ModuleEasyHiveBed()
	{
		super("Easy Hive bed", "Makes breaking beds from above on HiveMC easy", Category.MISC);
		this.registerModule();
		this.keyBind = Keyboard.KEY_O;
	}

	@Override
	public void onEnable() 
	{
		counter = 0;
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "EasyHiveBed" + EnumChatFormatting.WHITE + "]: EasyHiveBed enabled"));
	}

	@Override
	public void onDisable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "EasyHiveBed" + EnumChatFormatting.WHITE + "]: EasyHiveBed disabled"));
	}

	@Override
	public void onTick() 
	{
		if(counter == 5)
		{
			counter = 0;
			for(int x = radius; x >= -radius; x--)
	        {
				for(int y = radius; y >= -radius; y--)
	            {
	            	for(int z = radius; z >= -radius; z--)
	                {
	                	BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
	                	BlockPos blockAbove = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y + 1, mc.thePlayer.posZ + z);
	                	
	                	if(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed && mc.theWorld.getBlockState(blockAbove).getBlock().getMaterial() != Material.glass)
	                    {
	                		try
	                		{
	                			if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemCloth && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, EnumFacing.UP, new Vec3(0 + blockPos.getX(), -1 + blockPos.getY(), 0 + blockPos.getZ())))
	                            {
	                                mc.thePlayer.swingItem();
	                            }
	                		} 
	                		catch(Exception e)
	                 		{
	                			continue;
	                		}
	                    }
	                	
	                }
	            }
	        }
		}
		else
		{
			counter++;
		}
	}

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}
}
