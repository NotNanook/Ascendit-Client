package me.ascendit.module.modules.combat;

import java.util.Random;

import org.lwjgl.input.Mouse;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.util.MinecraftUtils;

@ModuleInfo(name = "AutoClicker", description = "Automatically clicks for you", category = Category.COMBAT)
public class AutoClicker extends Module {

	private final Random random = new Random();
	private long lastswing = 0L;

	@EventTarget
    public void onUpdate(final UpdateEvent event) {
		if (!Mouse.isButtonDown(0) || mc.thePlayer.isBlocking() || mc.currentScreen != null || !(System.currentTimeMillis() - lastswing >= random.nextInt(100)))
			return;
		MinecraftUtils.clickMouse();
		lastswing = System.currentTimeMillis();
	}
}