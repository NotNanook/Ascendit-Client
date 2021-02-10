package me.ascendit.modules.render;

import me.ascendit.Main;
import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModuleModuleList extends Module
{
	
	protected int textY;
	protected int width;
	protected int height;
	protected int textWidth;
	protected int textHeight;
	protected ScaledResolution resolution;
	
	public ModuleModuleList() {
		super("ModuleList", "Shows all active modules", Category.RENDER);
		this.registerModule();
	}

	@Override
	public void onEnable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "ModuleList" + EnumChatFormatting.WHITE + "]: ModuleList enabled"));

	}

	@Override
	public void onDisable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "ModuleList" + EnumChatFormatting.WHITE + "]: ModuleList disabled"));

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
	public void onRender(RenderGameOverlayEvent.Text event) 
	{
		textY = 1;
		
		resolution = event.resolution;
		width = resolution.getScaledWidth();
		height = resolution.getScaledHeight();
		
		for(Module module : Main.modules)
		{
			if(module.isEnabled())
			{
				textWidth = mc.fontRendererObj.getStringWidth(module.getName());
				textHeight = mc.fontRendererObj.FONT_HEIGHT;
				mc.fontRendererObj.drawString(module.getName(), width-(textWidth+1), textY, 0xf05454);
				textY += textHeight;
			}
		}
	}

}