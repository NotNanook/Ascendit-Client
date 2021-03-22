package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onInteractEvent {
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event)
	{
		for(Module module : Main.modules.moduleList)
		{
			if(module.isEnabled())
			{
				module.onInteract(event);
			}
		}
	}
}
