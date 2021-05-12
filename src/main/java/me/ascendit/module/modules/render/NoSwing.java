package me.ascendit.module.modules.render;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;

// mixin-based module
@ModuleInfo(name = "NoSwing", description = "Disabled the item swing animation", category = Category.RENDER)
public class NoSwing extends Module {
	public final BoolSetting serverside = new BoolSetting("ServerSide", true);
	
	public NoSwing() {
		addSettings(serverside);
	}
}