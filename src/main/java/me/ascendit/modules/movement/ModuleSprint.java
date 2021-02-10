package me.ascendit.modules.movement;

import org.lwjgl.input.Keyboard;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleSprint extends Module {

    public ModuleSprint() 
    {
        super("Sprint", "Makes you sprint all the time", Category.MOVEMENT);
        this.registerModule();
    }

    @Override
    public void onEnable() 
    {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "Sprint" + EnumChatFormatting.WHITE + "]: Sprint enabled"));
    }

    @Override
    public void onDisable() 
    {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "Sprint" + EnumChatFormatting.WHITE + "]: Sprint disabled"));
    }

    @Override
    public void onTick() 
    {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) 
    {
    }
    
    @Override
	public void onRender(RenderGameOverlayEvent.Text event) 
	{
	}
}
