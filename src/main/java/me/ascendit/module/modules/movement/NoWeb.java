package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.client.entity.EntityPlayerSP;

@ModuleInfo(name = "NoWeb", description = "Lets you run through Cobwebs", category = Category.MOVEMENT)
public class NoWeb extends Module {
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		try {
			MinecraftUtils.getField(EntityPlayerSP.class, "isInWeb").setBoolean(mc.thePlayer, false);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}