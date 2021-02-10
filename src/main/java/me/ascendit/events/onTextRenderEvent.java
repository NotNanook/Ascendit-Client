package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onTextRenderEvent 
{
	@SubscribeEvent
	public void onTextRender(RenderGameOverlayEvent.Text event)
	{
		for(Module module : Main.modules)
		{
			if(module.isEnabled())
			{
				module.onRender(event);
			}
		}
	}
}
