package me.ascendit.events;

import org.lwjgl.input.Keyboard;

import me.ascendit.Main;
import me.ascendit.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class onKeyInputEvent {
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent e)
	{
		for(Module module : Main.modules.moduleList)
		{
			if(module.getKeyBind() == Keyboard.getEventKey() && Keyboard.getEventKeyState())
			{
				if(module.isEnabled())
				{
					module.disable();
				}
				else
				{
					module.enable();
				}
			}
		}
	}
}
