package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class onRender3d 
{
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent event)
	{
		for(Module module : Main.modules.moduleList)
		{
			if(module.isEnabled())
			{
				module.onRender3d(event);
			}
		}
	}
}
