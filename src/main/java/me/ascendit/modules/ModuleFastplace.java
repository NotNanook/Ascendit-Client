package me.ascendit.modules;

public class ModuleFastplace extends Module
{
	public ModuleFastplace() 
	{
		super("Fastplace", "Allows you to set the place delay to whatever you want", Category.MISC);
		this.registerModule();
	}

	@Override
	public void onTick() 
	{
	}
}
