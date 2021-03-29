package me.ascendit.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import me.ascendit.Main;
import net.minecraft.client.multiplayer.WorldClient;

@Mixin(WorldClient.class)
public class MixinWorldClient {

	@ModifyVariable(method = "doVoidFogParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;randomDisplayTick(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V", shift = At.Shift.AFTER), ordinal = 0)
    private boolean handleBarriers(final boolean flag) {
		if(Main.trueSight.isEnabled())
		{
			return Main.trueSight.isBarrierVisible();
		}
		return flag;
    }
}
