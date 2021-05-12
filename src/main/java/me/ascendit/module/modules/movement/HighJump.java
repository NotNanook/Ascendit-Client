package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.ModeSetting;

@ModuleInfo(name = "HighJump", description = "Lets you jump higher", category = Category.MOVEMENT)
public class HighJump extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", "damage");
	public final FloatSetting height = new FloatSetting("DamageHeight", 2F, 1.1F, 10.0F);
	
	public HighJump() {
		addSettings(mode, height);
	}
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		addSettingtoName(mode);
		if (mode.getMode().equalsIgnoreCase("damage")) {
			if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround) {
				mc.thePlayer.motionY += 0.42F * height.get();
			}
		}
	}
}