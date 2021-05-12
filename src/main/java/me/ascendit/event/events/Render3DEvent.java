package me.ascendit.event.events;

import me.ascendit.event.Event;

// Called in MixinEntityRenderer
public class Render3DEvent extends Event {
	private float partialTicks;
	
	public Render3DEvent(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}