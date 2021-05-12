package me.ascendit.mixin.mixins.entity;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ascendit.module.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

@Mixin(Entity.class)
public abstract class MixinEntity {
	
    @Shadow
    public double posX;

    @Shadow
    public double posY;

    @Shadow
    public double posZ;

    @Shadow
    public float rotationPitch;
    
    @Shadow
    public float rotationYaw;
    
    @Shadow
    public double motionX;
    
    @Shadow
    public double motionY;
    
    @Shadow
    public double motionZ;
    
    @Shadow
    public boolean onGround;
    
    @Shadow
    public float prevRotationPitch;
    
    @Shadow
    public float prevRotationYaw;
    
    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();

    @Shadow
    public Entity ridingEntity;

    @Shadow
    public boolean isInWeb;

    @Shadow
    public boolean isCollidedHorizontally;

    @Shadow
    protected Random rand;

    @Shadow
    protected boolean inPortal;

    @Shadow
    public int timeUntilPortal;

    @Shadow
    public float width;

    @Shadow
    public abstract boolean isRiding();

    @Shadow
    public abstract boolean isSneaking();
    
    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
    
    @Inject(method = "getCollisionBorderSize", at = @At("HEAD"), cancellable = true)
    private void getCollisionBorderSize(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ModuleManager.hitbox.isToggled()) {
        	callbackInfoReturnable.setReturnValue(0.1F + ModuleManager.hitbox.size.get());
        }
    }
    
    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    private void setAngles(final float yaw, final float pitch, final CallbackInfo callbackInfo) {
        if (ModuleManager.nopitchlimit.isToggled()) {
            callbackInfo.cancel();

            float f = rotationPitch;
            float f1 = rotationYaw;
            rotationYaw = (float) ((double) rotationYaw + (double) yaw * 0.15D);
            rotationPitch = (float) ((double) rotationPitch - (double) pitch * 0.15D);
            prevRotationPitch += rotationPitch - f;
            prevRotationYaw += rotationYaw - f1;
        }
    }
}