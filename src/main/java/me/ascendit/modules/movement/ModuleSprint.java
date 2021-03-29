package me.ascendit.modules.movement;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.settings.KeyBinding;

public class ModuleSprint extends Module {

    public ModuleSprint() 
    {
        super("Sprint", "Makes you sprint all the time", Category.MOVEMENT);
        this.registerModule();
    }
    
    @Override
    public void onEnable() 
    {
    }
    
    @Override
    public void onDisable() 
    {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);    
    }
    
    @Override
    public void onTick() 
    {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
}
