package me.ascendit.setting;

import me.ascendit.Ascendit;

import net.minecraft.client.Minecraft;

public class BoolSetting extends Setting {
	
	private boolean value;
	
	public BoolSetting(String name, boolean value) {
		this.name = name;
		this.value = value;
	}

	public boolean get() {
		return value;
	}

	public void set(boolean value) {
		this.value = value;
	}
	
	public void toggle() {
		value = !value;
		Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5F, 1F);
		Ascendit.saveload.save();
	}
}