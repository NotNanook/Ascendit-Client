package me.ascendit.mixin.mixins.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ascendit.module.ModuleManager;
import net.minecraft.block.Block;

@Mixin(Block.class)
public class MixinBlock {
	
	@Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldSideBeRendered(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ModuleManager.xray.isToggled())
            callbackInfoReturnable.setReturnValue(ModuleManager.xray.xrayblocks.contains((Block) (Object) this));
    }
	
	@Inject(method = "getAmbientOcclusionLightValue", at = @At("HEAD"), cancellable = true)
    private void getAmbientOcclusionLightValue(final CallbackInfoReturnable<Float> floatCallbackInfoReturnable) {
        if (ModuleManager.xray.isToggled())
            floatCallbackInfoReturnable.setReturnValue(1F);
    }
}
