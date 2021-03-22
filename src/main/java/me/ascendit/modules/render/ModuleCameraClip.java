package me.ascendit.modules.render;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleCameraClip extends Module 
{
	public ModuleCameraClip() 
	{
		super("CameraClip", "Your camera will not be bothered by blocks", Category.RENDER);
		this.registerModule();
	}
}
