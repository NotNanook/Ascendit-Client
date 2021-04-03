package me.ascendit.modules.render;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleCustomHotbar extends Module
{
	public ModuleCustomHotbar()
	{
		// Mixin based module
		super("Customhotbar", "Replaces the original hotbar with a custom one", Category.RENDER);
		this.registerModule();
	}
}
