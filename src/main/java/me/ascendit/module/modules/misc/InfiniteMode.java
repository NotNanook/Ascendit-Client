package me.ascendit.module.modules.misc;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.Setting;

@ModuleInfo(name = "InfiniteMode", description = "No more min/max restrictions for settings", category = Category.MISC)
public class InfiniteMode extends Module {
	
	@Override
	public void onEnable() {
		for (final Module module: ModuleManager.modules) {
			for (final Setting setting: module.getSettings()) {
				if (setting instanceof DoubleSetting) {
					final DoubleSetting doublesetting = (DoubleSetting) setting;
					doublesetting.setMaximum(Double.MAX_VALUE);
					doublesetting.setMinimum(0.0);
				}
				if (setting instanceof IntSetting) {
					final IntSetting integersetting = (IntSetting) setting;
					integersetting.setMaximum(Integer.MAX_VALUE);
					integersetting.setMinimum(0);
				}
				if (setting instanceof FloatSetting) {
					final FloatSetting floatsetting = (FloatSetting) setting;
					floatsetting.setMaximum(Float.MAX_VALUE);
					floatsetting.setMinimum(0F);
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		for (final Module module: ModuleManager.modules) {
			for (final Setting setting: module.getSettings()) {
				if (setting instanceof DoubleSetting) {
					final DoubleSetting doublesetting = (DoubleSetting) setting;
					doublesetting.setMaximum(doublesetting.getDefaultMaximum());
					doublesetting.setMinimum(doublesetting.getDefaultMinimum());
				}
				if (setting instanceof IntSetting) {
					final IntSetting integersetting = (IntSetting) setting;
					integersetting.setMaximum(integersetting.getDefaultMaximum());
					integersetting.setMinimum(integersetting.getDefaultMinimum());
				}
				if (setting instanceof FloatSetting) {
					final FloatSetting floatsetting = (FloatSetting) setting;
					floatsetting.setMaximum(floatsetting.getDefaultMaximum());
					floatsetting.setMinimum(floatsetting.getDefaultMinimum());
				}
			}
		}
	}
}