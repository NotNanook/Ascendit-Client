package me.ascendit.setting;

import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;

public class Setting {
	
	protected String name;
	public boolean focused;
	
	public String getName() {
		return name;
	}
	
	public static Setting getSettingByName(Module m, String name) {
		for (Module module : ModuleManager.modules) {
			for(Setting setting : module.getSettings()){
				if(setting.getName().equalsIgnoreCase(name) && module == m){
					return setting;
				}
			}
		}
		return null;
	}
}