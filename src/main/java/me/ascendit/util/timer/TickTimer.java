package me.ascendit.util.timer;

public class TickTimer {

	private int tick;

	public void update() {
		tick++;
	}

	public void reset() {
		tick = 0;
	}

	public boolean hasTimeElapsed(int ticks) {
		return tick >= ticks;
	}
}