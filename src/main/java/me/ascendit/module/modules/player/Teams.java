package me.ascendit.module.modules.player;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;

@ModuleInfo(name = "Teams", description = "Prevents modules from targeting your teammates", category = Category.PLAYER)
public class Teams extends Module {
	
	public final BoolSetting killaura = new BoolSetting("Killaura", true);
	public final BoolSetting esp = new BoolSetting("ESP", true);

	public Teams() {
		addSettings(killaura, esp);
	}
}