package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class onTickEvent
{
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent e)
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
