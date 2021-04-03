package me.ascendit.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ascendit.Main;
import net.minecraft.block.BlockLiquid;

@Mixin(BlockLiquid.class)
public class MixinLiquids
{
	@Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    private void onCollideCheck(CallbackInfoReturnable<Boolean> callbackInfoReturnable) 
	{
        if(Main.liquidPlace.isEnabled())
        {
        	callbackInfoReturnable.setReturnValue(true);
        }
    }
}
