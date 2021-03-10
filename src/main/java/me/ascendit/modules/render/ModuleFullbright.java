package me.ascendit.modules.render;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "Fullbright" + EnumChatFormatting.WHITE + "]: Fullbright enabled"));
    }

    @Override
    public void onDisable() 
    {
        mc.gameSettings.gammaSetting = 1f;
        mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "Fullbright" + EnumChatFormatting.WHITE + "]: Fullbright disabled"));
    }

    @Override
    public void onTick() 
    {
    }

    @Override
    public void onInteract(PlayerInteractEvent event) 
    {
    }
    
    @Override
	public void onRender2d(RenderGameOverlayEvent.Text event) 
	{
	}
    
    @Override
	public void onRender3d(RenderWorldLastEvent event)
	{	
	}
}
