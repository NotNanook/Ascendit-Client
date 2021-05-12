package me.ascendit.module.modules.player;

import org.lwjgl.input.Keyboard;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "Eagle", description = "Fastbridges for you", category = Category.PLAYER)
public class Eagle extends Module {
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (mc.thePlayer.capabilities.isFlying || !MinecraftUtils.isHoldingblock() || !mc.thePlayer.onGround)
			return;
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir);
	}
	
	@Override
	public void onDisable() {
		if (mc.thePlayer == null)
			return;
		
		if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()))
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
	}
}