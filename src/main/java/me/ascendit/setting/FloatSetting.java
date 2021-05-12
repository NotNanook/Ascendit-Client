package me.ascendit.setting;

import me.ascendit.Ascendit;

import net.minecraft.client.Minecraft;

public class FloatSetting extends Setting {
	
	private float value, minimum, maximum, increment;
	private final float defaultvalue, defaultminimum, defaultmaximum;
	
	public FloatSetting(final String name, final float value, final float minimum, final float maximum, final float increment) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
		this.defaultvalue = value; this.defaultminimum = minimum; this.defaultmaximum = maximum;
	}
	
	public FloatSetting(final String name, final float value, final float minimum, final float maximum) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = 0.1F;
		this.defaultvalue = value; this.defaultminimum = minimum; this.defaultmaximum = maximum;
	}
	
	public FloatSetting(final String name, final float value) {
		this.name = name;
		this.value = value;
		this.minimum = 0.0F;
		this.maximum = Float.MAX_VALUE;
		this.increment = 0.1F;
		this.defaultvalue = value; this.defaultminimum = minimum; this.defaultmaximum = maximum;
	}

	public float get() {
		return value;
	}

	public void set(final float value) {
		final float precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum, value)) * precision) / precision;
	}
	
	public void increment(final boolean positive) {
		set(get() + (positive ? 1: -1) * increment);
		Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5F, 1F);
		Ascendit.saveload.save();
	}

	public float getMinimum() {
		return minimum;
	}

	public void setMinimum(final float minimum) {
		this.minimum = minimum;
	}

	public float getMaximum() {
		return maximum;
	}

	public void setMaximum(final float maximum) {
		this.maximum = maximum;
	}

	public float getIncrement() {
		return increment;
	}

	public void setIncrement(final float increment) {
		this.increment = increment;
	}

	public float getDefaultValue() {
		return defaultvalue;
	}

	public float getDefaultMinimum() {
		return defaultminimum;
	}

	public float getDefaultMaximum() {
		return defaultmaximum;
	}
}