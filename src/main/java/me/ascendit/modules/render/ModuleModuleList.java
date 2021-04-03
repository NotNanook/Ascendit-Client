package me.ascendit.modules.render;

import java.awt.Color;

import me.ascendit.Main;
import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ModuleModuleList extends Module
{
	
	private int textY;
	private int width;
	private int textWidth;
	private int textHeight;
	private ScaledResolution resolution;
	private int color = 0;
	private float counter = 0;
	
	public ModuleModuleList() 
	{
		super("ModuleList", "Shows all active modules", Category.RENDER);
		this.registerModule();
		
		// make it turned on by default
		this.enabled = true;
		
		// make i invisible in the modulelist
		this.visible = false;
	}
	
	@Override
	public void onRender2d(RenderGameOverlayEvent.Text event) 
	{
    	
    	if (!mc.gameSettings.showDebugInfo) {
    		
    		textY = 1;
    		counter += 0.001;
    		
    		if (counter >= 1) {
    			counter = 0;
    		}
    		
    		color = Color.HSBtoRGB(counter, 1, 1);
    		resolution = event.resolution;
    		width = resolution.getScaledWidth();
			textHeight = mc.fontRendererObj.FONT_HEIGHT;
    		int count = 0;
    		
    		for (Module module : Main.modules.moduleList) 
    		{
    			if (module.isEnabled() && module.isVisible()) 
    			{
					textWidth = mc.fontRendererObj.getStringWidth(module.getName());
					
					Gui.drawRect(width - (textWidth + 1) - 8, count * (mc.fontRendererObj.FONT_HEIGHT + 4), width - textWidth - 7, 4 + textHeight + count * (mc.fontRendererObj.FONT_HEIGHT + 4), color);
					Gui.drawRect(width - (textWidth + 1) - 6, count * (mc.fontRendererObj.FONT_HEIGHT + 4), width, 4 + textHeight + count * (mc.fontRendererObj.FONT_HEIGHT + 4), Integer.MIN_VALUE);
					mc.fontRendererObj.drawString(module.getName(), width - (textWidth + 1), textY + 1, color, true);
					
					count++;
					textY += textHeight + 4;
    			}
    		}
    	}
    }
}
