package me.ascendit.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ascendit.module.ModuleManager;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity {
	
    @Shadow
    public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    @Shadow
    public abstract boolean isPotionActive(Potion potionIn);
    
    @Shadow
    private int jumpTicks;
    
    @Shadow
    protected boolean isJumping;
    
    @Shadow
    protected void jump() {}

    @Shadow
    public void onLivingUpdate() {}

    @Shadow
    public abstract ItemStack getHeldItem();
    
    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void headLiving(CallbackInfo callbackInfo) {
        if (ModuleManager.nojumpdelay.isToggled())
            jumpTicks = 0;
    }
    
    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;isJumping:Z", ordinal = 1))
    private void onJumpSection(CallbackInfo callbackInfo) {
        if (ModuleManager.airjump.isToggled() && isJumping && this.jumpTicks == 0) {
            this.jump();
            this.jumpTicks = 10;
        }
    }

    @Inject(method = "isPotionActive(Lnet/minecraft/potion/Potion;)Z", at = @At("HEAD"), cancellable = true)
    private void isPotionActive(Potion p_isPotionActive_1_, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if ((p_isPotionActive_1_ == Potion.confusion || p_isPotionActive_1_ == Potion.blindness) && ModuleManager.antiblind.isToggled())
            callbackInfoReturnable.setReturnValue(false);
    }
    
    /**
     * @author Myzo
     * @reason Get the look of the Player based of the actual rotationYaw not rotationYawHead which makes the Rotations possible
     */
	@Overwrite
	public Vec3 getLook(float partialTicks) {
		if (partialTicks == 1.0F) {
			return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
		}
		float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
		float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
		return this.getVectorForRotation(f, f1);
	}
}