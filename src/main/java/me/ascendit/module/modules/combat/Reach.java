package me.ascendit.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.TickEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.FloatSetting;

@ModuleInfo(name = "Reach", description = "Allows you to reach further", category = Category.COMBAT)
public class Reach extends Module {
	
	public final FloatSetting combatreach = new FloatSetting("CombatReach", 3.95F, 3.0F, 10.0F, 0.01F);
	public final FloatSetting buildreach = new FloatSetting("BuildReach", 5.0F, 3.0F, 10.0F, 0.01F);

	public Reach() {
		addSettings(combatreach, buildreach);
	}
	
	@EventTarget
	public void onTick(final TickEvent event) {
		setDisplayName(getName() + " " + ChatFormatting.GRAY + combatreach.get() + " | " + buildreach.get());
	}
	
	public float getMaxReach() {
		return combatreach.get() > buildreach.get() ? combatreach.get() : buildreach.get();
	}
}