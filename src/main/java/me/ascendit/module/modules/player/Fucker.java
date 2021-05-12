package me.ascendit.module.modules.player;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.IntSetting;

import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "Fucker", description = "Automatically destroys beds", category = Category.PLAYER)
public class Fucker extends Module {
	
	private final BoolSetting noswing = new BoolSetting("NoSwing", false);
	private final IntSetting range = new IntSetting("Range", 4, 1, 10);
	
	private float currentdamage = 0F;

	public Fucker() {
		addSettings(noswing, range);
	}
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		for (int x = range.get(); x >= -range.get(); x--) {
			for (int y = range.get(); y >= -range.get(); y--) {
				for (int z = range.get(); z >= -range.get(); z--) {
					final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
					if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed) {
						if (currentdamage == 0F) {
							mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
						}
						
						if (!noswing.get())
							mc.thePlayer.swingItem();
						
						currentdamage += mc.theWorld.getBlockState(blockPos).getBlock().getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos);
					    mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), blockPos, (int) ((currentdamage * 10F) - 1));
					    
					    if (currentdamage >= 1F) {
					    	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
					    	mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
					    	currentdamage = 0F;
					    }
					}
				}
			}
		}
    }
}