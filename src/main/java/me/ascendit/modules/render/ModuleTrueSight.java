package me.ascendit.modules.render;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleTrueSight extends Module
{
	private boolean barrierVis;
	private boolean entityVis;
	
	public ModuleTrueSight()
	{
		super("TrueSight", "Allows you to see invisible entities and barriers", Category.RENDER);
		this.registerModule();
		
		barrierVis = true;
		entityVis = true;
	}
	
	public boolean isBarrierVisible()
	{
		return barrierVis;
	}
	
	public boolean isEntityVisible()
	{
		return entityVis;
	}
}
