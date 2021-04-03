package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.commands.Command;
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
		
		for(Command command : Main.commands)
		{
			if(command.isToggled())
			{
				command.onRender2d(event);
			}
		}
	}
}
