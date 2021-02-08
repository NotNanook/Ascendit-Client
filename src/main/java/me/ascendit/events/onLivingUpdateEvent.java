package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onLivingUpdateEvent
{
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e)
	{
		for(Module module : Main.modules)
		{
			if(module.isEnabled())
			{
				module.onTick();
			}
		}
	}
}
