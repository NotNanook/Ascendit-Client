package me.ascendit.module.modules.render;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;

// mixin-based module
@ModuleInfo(name = "Truesight", description = "Allows you to see barriers all the time", category = Category.RENDER)
public class Truesight extends Module {
	
	public final BoolSetting barriers = new BoolSetting("Barriers", true);
	public final BoolSetting entities = new BoolSetting("Entities", true);
	
	public Truesight() {
		addSettings(barriers, entities);
	}
}