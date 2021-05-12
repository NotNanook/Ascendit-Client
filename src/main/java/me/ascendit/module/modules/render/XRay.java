package me.ascendit.module.modules.render;

import java.util.concurrent.CopyOnWriteArrayList;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

import net.minecraft.block.Block;

@ModuleInfo(name = "XRay", description = "Renders certain blocks through walls", category = Category.RENDER)
public class XRay extends Module {
	
	public CopyOnWriteArrayList<Block> xrayblocks = new CopyOnWriteArrayList<Block>();

	@Override
	public void onToggle() {
		mc.renderGlobal.loadRenderers();
	}
}