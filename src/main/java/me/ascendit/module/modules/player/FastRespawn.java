package me.ascendit.module.modules.player;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

@ModuleInfo(name = "FastRespawn", description = "Makes you respawn faster", category = Category.PLAYER)
public class FastRespawn extends Module {
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		if (mc.thePlayer.isEntityAlive()) return;
		mc.thePlayer.respawnPlayer();
	}
}
