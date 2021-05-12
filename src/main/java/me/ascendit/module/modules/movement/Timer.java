package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.FloatSetting;

@ModuleInfo(name = "Timer", description = "Changes the speed of the game", category = Category.MOVEMENT)
public class Timer extends Module {
	
	private final FloatSetting speed = new FloatSetting("Speed", 1.0F, 0.1F, 100.0F);
	
	public Timer() {
		addSettings(speed);
	}
	
	@Override
	public void onDisable() {
		if (mc.thePlayer != null)
			timer.timerSpeed = 1F;
	}
	
	@EventTarget
	public void onUpdate(final UpdateEvent event) {
		addSettingtoName(speed);
		if (timer.timerSpeed != speed.get())
			timer.timerSpeed = speed.get();
	}
}