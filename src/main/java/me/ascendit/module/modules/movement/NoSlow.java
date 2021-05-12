package me.ascendit.module.modules.movement;

import org.lwjgl.input.Keyboard;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;

// mixin-based module
@ModuleInfo(name = "NoSlow", description = "Allows you to always maintain full speed", category = Category.MOVEMENT)
public class NoSlow extends Module {
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		if (ModuleManager.sprint.isToggled()) {
			if (!mc.thePlayer.isSprinting() && mc.thePlayer.moveForward > 0 && mc.thePlayer.isUsingItem()) {
				mc.thePlayer.setSprinting(true);
			}
		}
		if (!mc.thePlayer.isSprinting() && mc.thePlayer.moveForward > 0 && mc.thePlayer.isUsingItem() && Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode())) {
			mc.thePlayer.setSprinting(true);
		}
	}
}