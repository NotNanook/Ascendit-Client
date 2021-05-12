package me.ascendit.module.modules.combat;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.TickEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.FloatSetting;

@ModuleInfo(name = "HitBox", description = "Expands hitboxes of entities", category = Category.COMBAT)
public class HitBox extends Module {
	
	public final FloatSetting size = new FloatSetting("Size", 0.4F, 0.1F, 1.0F);

	public HitBox() {
		addSettings(size);
	}
	
	@EventTarget
	public void onTick(final TickEvent event) {
		addSettingtoName(size);;
	}
}