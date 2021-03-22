package me.ascendit.modules.combat;

import org.lwjgl.input.Mouse;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.settings.KeyBinding;

public class ModuleAutoclicker extends Module
{
	public ModuleAutoclicker()
	{
		super("Autoclicker", "Automatically left-clicks for you when you hold it down", Category.COMBAT);
		this.registerModule();
	}

    public void onTick()
    {
        if(!mc.inGameHasFocus || mc.thePlayer.isBlocking())
        {
        	return;
        }
        
        if(Mouse.isButtonDown(0)) 
        {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
        }   
        else if(Mouse.isButtonDown(1))
        {
        	KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
        }
    }
}
