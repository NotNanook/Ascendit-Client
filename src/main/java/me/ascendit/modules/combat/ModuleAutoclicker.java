package me.ascendit.modules.combat;

import org.lwjgl.input.Mouse;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleAutoclicker extends Module
{
	public ModuleAutoclicker()
	{
		super("Autoclicker", "Automatically left-clicks for you when you hold it down", Category.COMBAT);
		this.registerModule();
	}

	@Override
	public void onEnable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "Autoclicker" + EnumChatFormatting.WHITE + "]: Autoclicker enabled"));
	}

	@Override
	public void onDisable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "Autoclicker" + EnumChatFormatting.WHITE + "]: Autoclicker disabled"));
	}

	@Override
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

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}

	@Override
	public void onRender2d(Text event) 
	{
	}

	@Override
	public void onRender3d(RenderWorldLastEvent event) 
	{
	}
}
