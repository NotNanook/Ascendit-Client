package me.ascendit.event.events;

import me.ascendit.event.Event;

import net.minecraft.client.gui.ScaledResolution;

// Called in MixinGuiIngame
public class Render2DEvent extends Event {
	
	private ScaledResolution sr;
	private float partialTicks;
	
	public Render2DEvent(ScaledResolution sr, float partialTicks) {
		this.sr = sr;
		this.partialTicks = partialTicks;
	}
	
	public ScaledResolution getResolution() {
		return sr;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
}