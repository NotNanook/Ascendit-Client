package me.ascendit.modules.render;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleFullbright extends Module {

    public ModuleFullbright() 
    {
        super("Fullbright", "Brightens up the world around you", Category.RENDER);
        this.registerModule();
    }
    
    @Override
    public void onEnable() 
    {
        mc.gameSettings.gammaSetting = 100f;
    }
    
    @Override
    public void onDisable() 
    {
        mc.gameSettings.gammaSetting = 1f;
    }
}
