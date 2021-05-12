package me.ascendit.module.modules.player;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.TickEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.IntSetting;

// mixin-based module
@ModuleInfo(name = "Fastplace", description = "Allows you to set the block place delay", category = Category.PLAYER)
public class Fastplace extends Module {
	
	public final IntSetting delay = new IntSetting("Delay", 0, 0, 4);
	public final BoolSetting onlyblocks = new BoolSetting("Only Blocks", true);
    
    public Fastplace() {
        addSettings(delay, onlyblocks);
    }
    
    @EventTarget
	public void onTick(final TickEvent event) {
    	addSettingtoName(delay);
    }
}