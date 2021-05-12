package me.ascendit.module.modules.player;

import java.util.Random;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.MotionEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

@ModuleInfo(name = "Derp", description = "Makes you derp around", category = Category.PLAYER)
public class Derp extends Module {
	
	private final Random rd = new Random();

	@EventTarget
	public void onMotion(final MotionEvent event) {
		event.setYaw(rd.nextFloat()*360);
		event.setPitch(mc.thePlayer.cameraPitch+(rd.nextFloat()*360)-180);
	}
}