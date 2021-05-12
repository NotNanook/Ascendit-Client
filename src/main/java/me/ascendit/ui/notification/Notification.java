package me.ascendit.ui.notification;

import java.awt.Font;

import me.ascendit.module.ModuleManager;
import me.ascendit.ui.font.FontRenderer;
import me.ascendit.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private String message;
	private long start;
	
	private long fadedIn;
	private long fadeOut;
	private long end;
	
	private FontRenderer fontrenderer = new FontRenderer("Comfortaa", Font.PLAIN, 20);
	
	public Notification(String message, int length) {
		this.message = message;
		
		fadedIn = 200 * length;
		fadeOut = fadedIn + 500 * length;
		end = fadeOut + fadedIn;
	}
	
	public void show() {
		start = System.currentTimeMillis();
	}
	
	public boolean isShown() {
		return getTime() <= end;
	}
	
	private long getTime() {
		return System.currentTimeMillis() - start;
	}
	
	public void render() {
		ScaledResolution sr = new ScaledResolution(mc);
		
		double offset = 0;
		int width = 100;
		int height = 20;
		long time = getTime();
		
		if (time < fadedIn) {
			offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
		} else if (time > fadeOut) {
			offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width); 
		} else {
			offset = width;
		}
		
		RenderUtils.drawRect(sr.getScaledWidth() - offset - 30, sr.getScaledHeight() - (ModuleManager.hud.playermodel.get() ? 165 : 50) - height, sr.getScaledWidth(), sr.getScaledHeight() - (ModuleManager.hud.playermodel.get() ? 165 : 50), 0xCC000000);
		RenderUtils.drawRect(sr.getScaledWidth() - offset - 30 - 2, sr.getScaledHeight() - (ModuleManager.hud.playermodel.get() ? 165 : 50) - height, sr.getScaledWidth() - offset - 30 + 1, sr.getScaledHeight() - (ModuleManager.hud.playermodel.get() ? 165 : 50), 0xFFFF0000);
		
		if (ModuleManager.hud.customfont.get())
			fontrenderer.drawString(message, (int) (sr.getScaledWidth() - offset - 30 + 7), sr.getScaledHeight() - height + 1 - (ModuleManager.hud.playermodel.get() ? 163 : 48), 0xA0A0A0);
		else
			mc.fontRendererObj.drawString(message, (int) (sr.getScaledWidth() - offset - 30 + 7), sr.getScaledHeight() - height + 1 - (ModuleManager.hud.playermodel.get() ? 160 : 45), 0xA0A0A0);
	}
}