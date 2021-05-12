package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.IntSetting;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(name = "AutoMLG", description = "Automatically saves you with a cobweb/waterbucket/ladder", category = Category.MOVEMENT)
public class AutoMLG extends Module {
	
	private final IntSetting falldistance = new IntSetting("Falldistance", 10, 2, 20);
	
	public AutoMLG() {
		addSettings(falldistance);
	}
	
	@EventTarget
    public void onUpdate(final UpdateEvent event) {
		addSettingtoName(falldistance);
		final BlockPos BlockInPlayer = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
		
		if (mc.thePlayer.fallDistance < falldistance.get() || mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying || mc.thePlayer.inventory.getCurrentItem() == null || mc.theWorld.getBlockState(BlockInPlayer).getBlock().getMaterial() == Material.web || mc.theWorld.getBlockState(BlockInPlayer).getBlock().getMaterial() == Material.water)
			return;
		
		if (mc.thePlayer.motionY < -3.87)
			mc.thePlayer.motionY = 0F;
		
		if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
			return;
		
		if (mc.thePlayer.inventory.getCurrentItem().getItem() == Item.getItemById(/*cobweb*/ 30) || mc.thePlayer.inventory.getCurrentItem().getItem() == Item.getItemById(/*ladder*/ 65))
			MinecraftUtils.rightClickMouse();
		else if (mc.thePlayer.inventory.getCurrentItem().getItem() == Item.getItemById(/*water bucket*/ 326))
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
	}
}