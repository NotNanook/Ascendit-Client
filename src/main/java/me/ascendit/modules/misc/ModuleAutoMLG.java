package me.ascendit.modules.misc;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleAutoMLG extends Module
{
	private final int EMPTY_WATER_BUCKET = 325;
	private int minFallDst;
	
	public ModuleAutoMLG()
	{
		super("AutoMLG", "Automatically saves you with cobwebs/waterbuckets", Category.MISC);
		this.registerModule();
	}
	
	@Override
	public void onTick()
	{
	}
}
