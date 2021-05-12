package me.ascendit.module.modules.render;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.ModeSetting;

// mixin-based module
@ModuleInfo(name = "Chams", description = "Allows you to see entities through blocks", category = Category.RENDER)
public class Chams extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", "players", "players", "entities");
	
	public Chams() {
		this.addSettings(mode);
	}
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		this.addSettingtoName(mode);
	}
}