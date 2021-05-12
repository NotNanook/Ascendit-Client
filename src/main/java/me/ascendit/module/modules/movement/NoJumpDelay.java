package me.ascendit.module.modules.movement;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

// mixin-based module
@ModuleInfo(name = "NoJumpDelay", description = "Removes the delay between jumps", category = Category.MOVEMENT)
public class NoJumpDelay extends Module {}