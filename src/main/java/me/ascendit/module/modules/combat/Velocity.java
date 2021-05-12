package me.ascendit.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.ModeSetting;

@ModuleInfo(name = "Velocity", description = "Decreases received knockback", category = Category.COMBAT)
public class Velocity extends Module {
	
	private final DoubleSetting horizontal = new DoubleSetting("Horizontal", 0, 0, 1);
	private final DoubleSetting vertical = new DoubleSetting("Vertical", 0, 0, 1);
	private final ModeSetting mode = new ModeSetting("Mode", "vanilla", "ongroundspoof");
	
	public Velocity() {
		addSettings(horizontal, vertical, mode);
	}

	@EventTarget
    public void onUpdate(UpdateEvent event) {
		setDisplayName(getName() + " " + ChatFormatting.GRAY + "H: " + horizontal.get() + " V: " + vertical.get());
		switch (mode.getMode().toLowerCase()) {
			case "vanilla":
				if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
					mc.thePlayer.motionX *= horizontal.get();
					mc.thePlayer.motionY *= vertical.get();
					mc.thePlayer.motionZ *= horizontal.get();
				}
				break;
			case "ongroundspoof":
				if (mc.thePlayer.hurtTime > 0)
					mc.thePlayer.onGround = true;
				break;
		}
	}
}