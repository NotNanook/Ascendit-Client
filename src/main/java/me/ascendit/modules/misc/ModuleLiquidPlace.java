package me.ascendit.modules.misc;

import org.lwjgl.input.Keyboard;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;

public class ModuleLiquidPlace extends Module
{
	public ModuleLiquidPlace()
	{
		// Mixin based module
		super("LiquidPlace", "Allows you to place blocks on liquids", Category.MISC);
		this.registerModule();
		this.keyBind = Keyboard.KEY_O;
	}
}
