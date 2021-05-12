package me.ascendit.mixin.mixins.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ascendit.module.ModuleManager;
import net.minecraft.block.BlockLiquid;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {
	@Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    private void onCollideCheck(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(ModuleManager.liquidplace.isToggled()) {
        	callbackInfoReturnable.setReturnValue(true);
        }
    }
}