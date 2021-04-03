package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onLivingUpdateEvent
{
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e)
	{
		if(e.entity == Minecraft.getMinecraft().thePlayer)
		{
			for(Module module : Main.modules.moduleList)
			{
				if(module.isEnabled())
				{
					module.onTick();
				}
			}
		}
	}
}
