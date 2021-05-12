package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.ModeSetting;

import net.minecraft.client.settings.KeyBinding;

@ModuleInfo(name = "Sprint", description = "Makes you sprint all the time", category = Category.MOVEMENT)
public class Sprint extends Module {

	private final ModeSetting mode = new ModeSetting("Mode", "legit", "omni");
	// mixin-based setting
	public final BoolSetting nohunger = new BoolSetting("NoHunger", true);

	public Sprint() {
		addSettings(mode, nohunger);
	}

	@Override
	public void onDisable() {
		if (mode.getMode().equalsIgnoreCase("legit"))
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
		else if (mode.getMode().equalsIgnoreCase("omni"))
			mc.thePlayer.setSprinting(false);
	}

	@EventTarget
	public void onUpdate(final UpdateEvent event) {
		addSettingtoName(mode);
		if (mode.getMode().equalsIgnoreCase("legit"))
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
		else if (mode.getMode().equalsIgnoreCase("omni"))
			mc.thePlayer.setSprinting(true);
	}
}