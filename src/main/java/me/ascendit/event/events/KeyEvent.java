package me.ascendit.event.events;

import me.ascendit.event.Event;

public class KeyEvent extends Event {
	private final int key;
	
	public final int getKey() {
		return this.key;
	}
	
	public KeyEvent(int key) {
		this.key = key;
	}
}