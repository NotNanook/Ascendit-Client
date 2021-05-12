package me.ascendit.setting;

import me.ascendit.Ascendit;

import net.minecraft.client.Minecraft;

public class DoubleSetting extends Setting {
	
	private double value, minimum, maximum, increment;
	private final double defaultvalue, defaultminimum, defaultmaximum;
	
	public DoubleSetting(final String name, final double value, final double minimum, final double maximum, final double increment) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
		this.defaultvalue = value; this.defaultminimum = minimum; this.defaultmaximum = maximum;
	}
	
	public DoubleSetting(final String name, final double value, final double minimum, final double maximum) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = 0.1;
		this.defaultvalue = value; this.defaultminimum = minimum; this.defaultmaximum = maximum;
	}
	
	public DoubleSetting(final String name, final double value) {
		this.name = name;
		this.value = value;
		this.minimum = 0.0;
		this.maximum = Double.MAX_VALUE;
		this.increment = 0.1;
		this.defaultvalue = value; this.defaultminimum = minimum; this.defaultmaximum = maximum;
	}

	public double get() {
		return value;
	}

	public void set(final double value) {
		final double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum, value)) * precision) / precision;
	}
	
	public void increment(final boolean positive) {
		set(get() + (positive ? 1: -1) * increment);
		Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5F, 1F);
		Ascendit.saveload.save();
	}

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(final double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(final double maximum) {
		this.maximum = maximum;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(final double increment) {
		this.increment = increment;
	}

	public double getDefaultValue() {
		return defaultvalue;
	}

	public double getDefaultMinimum() {
		return defaultminimum;
	}

	public double getDefaultMaximum() {
		return defaultmaximum;
	}
}
