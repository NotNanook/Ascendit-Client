package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.ModeSetting;

@ModuleInfo(name = "Speed", description = "Lets you run faster", category = Category.MOVEMENT)
public class Speed extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", "legit", "lowhop");
	private final DoubleSetting lowhopheight = new DoubleSetting("LowHopHeight", 0.8, 0.1, 1.0);
	
	public Speed() {
		addSettings(mode, lowhopheight);
	}
	
	@EventTarget
	public void onUpdate(final UpdateEvent event) {
		addSettingtoName(mode);
		switch (mode.getMode().toLowerCase()) {
			case "legit":
				if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0) {
					mc.thePlayer.jump();
				}
			case "lowhop":
				if (mc.thePlayer.motionY > 0 && !mc.thePlayer.onGround && !mc.thePlayer.capabilities.isFlying) {
					mc.thePlayer.motionY *= lowhopheight.get();
				}
		}
	}
}