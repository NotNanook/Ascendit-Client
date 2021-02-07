package me.ascendit.modules.misc;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleFastplace extends Module
{
	private int delay;
	
	public ModuleFastplace() 
	{
		super("Fastplace", "Allows you to set the place delay to whatever you want", Category.MISC);
		this.registerModule();
	}

	@Override
	public void onTick() 
	{
		System.out.println("Fatplace on");
	}

	@Override
	public void onEnable() 
	{
	}

	@Override
	public void onDisable() 
	{
	}
}
