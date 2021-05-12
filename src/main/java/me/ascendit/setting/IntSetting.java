package me.ascendit.setting;

import me.ascendit.Ascendit;

import net.minecraft.client.Minecraft;

public class IntSetting extends Setting {
	
	private int value, minimum, maximum;
	private final int defaultvalue, defaultminimum, defaultmaximum;
	
	public IntSetting(final String name, final int value, final int minimum, final int maximum) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.defaultvalue = value;
		this.defaultminimum = minimum;
		this.defaultmaximum = maximum;
	}

	public IntSetting(final String name, final int value) {
		this.name = name;
		this.value = value;
		this.minimum = 0;
		this.maximum = Integer.MAX_VALUE;
		this.defaultvalue = value;
		this.defaultminimum = minimum;
		this.defaultmaximum = maximum;
	}

	public int get() {
		return value;
	}

	public void set(final int value) {
		this.value = Math.max(minimum, Math.min(maximum, value));
	}
	
	public void increment(final boolean positive) {
		set(get() + (positive ? 1: -1));
		Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5F, 1F);
		Ascendit.saveload.save();
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(final int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(final int maximum) {
		this.maximum = maximum;
	}

	public int getDefaultValue() {
		return defaultvalue;
	}

	public int getDefaultMinimum() {
		return defaultminimum;
	}

	public int getDefaultMaximum() {
		return defaultmaximum;
	}
}