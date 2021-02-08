package me.ascendit.modules.misc;

import org.lwjgl.input.Keyboard;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemCloth;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModuleEasyHiveBed extends Module{
	
	protected int radius = 3;
	
	public ModuleEasyHiveBed()
	{
		super("Easy Hive bed", "Makes breaking beds from above on HiveMC easy", Category.MISC);
		this.registerModule();
		this.keyBind = Keyboard.KEY_O;
	}

	@Override
	public void onEnable() 
	{
	}

	@Override
	public void onDisable() 
	{
	}

	@Override
	public void onTick() 
	{
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
                			if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemCloth && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, EnumFacing.UP, new Vec3(0 + blockPos.getX(), -2 + blockPos.getY(), 0 + blockPos.getZ())))
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

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}
}
