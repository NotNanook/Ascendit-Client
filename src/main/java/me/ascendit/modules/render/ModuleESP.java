package me.ascendit.modules.render;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModuleESP extends Module
{
	public ModuleESP()
	{
		super("ESP", "Allows you to see players through walls", Category.RENDER);
		this.registerModule();
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
	public void onTick() 
	{
	}

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}
}
