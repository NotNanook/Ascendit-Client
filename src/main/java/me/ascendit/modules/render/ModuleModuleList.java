package me.ascendit.modules.render;

import java.awt.Color;

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
	
	private int textY;
	private int width;
	private int height;
	private int textWidth;
	private int textHeight;
	private ScaledResolution resolution;
	private int color = 0;
	private float counter = 0;
	
	public ModuleModuleList() 
	{
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
		textY = 2;
		counter += 0.005;
		if(counter >= 1)
		{
			counter = 0;
		}	
		color = Color.HSBtoRGB(counter, 1, 1);
		
		resolution = event.resolution;
		width = resolution.getScaledWidth();
		height = resolution.getScaledHeight();
		
		for(Module module : Main.modules)
		{
			if(module.isEnabled())
			{
				textWidth = mc.fontRendererObj.getStringWidth(module.getName());
				textHeight = mc.fontRendererObj.FONT_HEIGHT;
				
				mc.fontRendererObj.drawString(module.getName(), width-(textWidth+2), textY, color, true);
				textY += textHeight+1;
			}
		}
	}
}
