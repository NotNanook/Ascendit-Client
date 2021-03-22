package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onRender2d 
{
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Text event)
	{
		for(Module module : Main.modules.moduleList)
		{
			if(module.isEnabled())
			{
				module.onRender2d(event);
			}
		}
	}
}
