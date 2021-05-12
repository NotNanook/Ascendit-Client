package me.ascendit.module.modules.render;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

// mixin-based module
@ModuleInfo(name = "NoHurtCam", description = "Disables the screen shake effect when getting hit", category = Category.RENDER)
public class NoHurtCam extends Module {}