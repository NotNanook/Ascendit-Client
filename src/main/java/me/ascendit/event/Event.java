package me.ascendit.event;

import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;

public class Event {
	
	protected final Minecraft mc = Minecraft.getMinecraft();
	private boolean cancelled;
	
	public Event call() {
		final CopyOnWriteArrayList<EventData> datalist = EventManager.get(this.getClass());
		
		if (datalist != null) {
			for (EventData data: datalist) {
				try {
					data.target.invoke(data.source, this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}
}