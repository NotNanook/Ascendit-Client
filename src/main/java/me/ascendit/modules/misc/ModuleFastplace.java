package me.ascendit.modules.misc;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

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
	}

	@Override
	public void onEnable() 
	{
	}

	@Override
	public void onDisable() 
	{
	}

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}
}
