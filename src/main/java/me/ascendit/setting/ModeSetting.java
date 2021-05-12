package me.ascendit.setting;

import java.util.Arrays;
import java.util.List;

import me.ascendit.Ascendit;

import net.minecraft.client.Minecraft;

public class ModeSetting extends Setting {
	
	private int modeindex;
	private final List<String> modes;
	
	public ModeSetting(final String name, final String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		this.modeindex = 0; 
	}
	
	public String getMode() {
		try {
			return modes.get(modeindex);
		} catch (final ArrayIndexOutOfBoundsException e) {
			return modes.get(0);
		}
	}
	
	public void setMode(final String mode) {
		try  {
			this.modeindex = modes.indexOf(mode); 
		} catch (final ArrayIndexOutOfBoundsException e) {
			return;
		}
	}
	
	public List<String> getModes() {
		return modes;
	}
	
	public void cycle() {
		if (modeindex < modes.size() - 1)
			modeindex++;
		else
			modeindex = 0;
		
		Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5F, 1F);
		Ascendit.saveload.save();
	}
}