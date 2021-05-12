package me.ascendit.module.modules.render;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.ModeSetting;

import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "Fullbright", description = "Brightens up the world around you", category = Category.RENDER)
public class Fullbright extends Module {

	private final ModeSetting mode = new ModeSetting("Mode", "gamma", "nightvision");

	public Fullbright() {
		addSettings(mode);
	}
	
	@Override
	public void onEnable() {
		if (mode.getMode().equalsIgnoreCase("gamma"))
			mc.gameSettings.gammaSetting = 100f;
	}

	@Override
	public void onDisable() {
		if (mode.getMode().equalsIgnoreCase("gamma")) {
			mc.gameSettings.gammaSetting = 1f;
			return;
		}
		if (mode.getMode().equalsIgnoreCase("nightvision")) {
			mc.thePlayer.removePotionEffect(16); 
			return;
		}
	}

	@EventTarget
    public void onUpdate(final UpdateEvent event) {
		addSettingtoName(mode);
		if (mode.getMode().equalsIgnoreCase("nightvision"))
			mc.thePlayer.addPotionEffect(new PotionEffect(/* id */ 16, /* duration */ 300, /* amplifier */ 255));
	}
}