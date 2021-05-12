package me.ascendit.mixin.mixins.render;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Predicates;

import me.ascendit.event.events.Render3DEvent;
import me.ascendit.module.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
	
	@Shadow
	private Minecraft mc;
	
	@Shadow
	private float thirdPersonDistanceTemp;
	
	@Shadow
    private float thirdPersonDistance;
	
	@Shadow
    private boolean cloudFog;
	
	@Shadow
    private Entity pointedEntity;
	
	@Shadow
	private long prevFrameTime;
	
	@Shadow
	private float smoothCamYaw;
	
	@Shadow
	private float smoothCamPitch;
	
	@Shadow
	private float smoothCamPartialTicks;
	
	@Shadow
	private float smoothCamFilterX;
	
	@Shadow
	private float smoothCamFilterY;
	
	@Shadow
	private static boolean anaglyphEnable;
	
	@Shadow
	private boolean useShader;
	
	@Shadow
	private ShaderGroup theShaderGroup;
	
	@Shadow
	private long renderEndNanoTime;
	
	@Shadow
	private void renderWorld(float partialTicks, long finishTimeNano) {}
	
	@Shadow
	private void setupOverlayRendering() {}
	
	@Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
	private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
		new Render3DEvent(partialTicks).call();
	}
	
	@Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
        if (ModuleManager.nohurtcam.isToggled()) {
        	callbackInfo.cancel();
        }
    }
	
	@Inject(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"), cancellable = true)
	private void cameraClip(float partialTicks, CallbackInfo callbackInfo) {
        if (ModuleManager.cameraclip.isToggled()) {
            callbackInfo.cancel();

            Entity entity = this.mc.getRenderViewEntity();
            float f = entity.getEyeHeight();

            if(entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
                f = (float) ((double) f + 1D);
                GlStateManager.translate(0F, 0.3F, 0.0F);

                if(!this.mc.gameSettings.debugCamEnable) {
                    BlockPos blockpos = new BlockPos(entity);
                    IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                    net.minecraftforge.client.ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);

                    GlStateManager.rotate(ModuleManager.perspective.getCameraYaw() + (ModuleManager.perspective.getCameraYaw() - ModuleManager.perspective.getCameraYaw()) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
                    GlStateManager.rotate(ModuleManager.perspective.getCameraPitch() + (ModuleManager.perspective.getCameraPitch() - ModuleManager.perspective.getCameraPitch()) * partialTicks, -1.0F, 0.0F, 0.0F);
                }
            }else if(this.mc.gameSettings.thirdPersonView > 0) {
                double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;

                if(this.mc.gameSettings.debugCamEnable) {
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                }else{
                    float f1 = ModuleManager.perspective.getCameraYaw();
                    float f2 = ModuleManager.perspective.getCameraPitch();

                    if(this.mc.gameSettings.thirdPersonView == 2)
                        f2 += 180.0F;

                    if(this.mc.gameSettings.thirdPersonView == 2)
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

                    GlStateManager.rotate(ModuleManager.perspective.getCameraPitch() - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(ModuleManager.perspective.getCameraYaw() - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - ModuleManager.perspective.getCameraYaw(), 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - ModuleManager.perspective.getCameraPitch(), 1.0F, 0.0F, 0.0F);
                }
            }else
                GlStateManager.translate(0.0F, 0.0F, -0.1F);

            if(!this.mc.gameSettings.debugCamEnable) {
                float yaw = ModuleManager.perspective.getCameraYaw() + (ModuleManager.perspective.getCameraYaw() - ModuleManager.perspective.getCameraYaw()) * partialTicks + 180.0F;
                float pitch = ModuleManager.perspective.getCameraPitch() + (ModuleManager.perspective.getCameraPitch() - ModuleManager.perspective.getCameraPitch()) * partialTicks;
                float roll = 0.0F;
                if(entity instanceof EntityAnimal) {
                    EntityAnimal entityanimal = (EntityAnimal) entity;
                    yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
                }

                Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
                EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer) (Object) this, entity, block, partialTicks, yaw, pitch, roll);
                MinecraftForge.EVENT_BUS.post(event);
                GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(event.pitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(event.yaw, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.translate(0.0F, -f, 0.0F);
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
            this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
        }
    }
	
	/**
     * @author Myzo
     * @reason Implement Perspective
     */
	@Overwrite
	private void orientCamera(float partialTicks) {
	    Entity entity = this.mc.getRenderViewEntity();
	    float f = entity.getEyeHeight();
	    double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
	    double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
	    double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;

	    if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
	        f = (float)((double) f + 1.0D);
	        GlStateManager.translate(0.0F, 0.3F, 0.0F);

	        if (!this.mc.gameSettings.debugCamEnable) {
	            BlockPos blockpos = new BlockPos(entity);
	            IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
	            net.minecraftforge.client.ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);

	            GlStateManager.rotate(ModuleManager.perspective.getCameraYaw() + (ModuleManager.perspective.getCameraYaw() - ModuleManager.perspective.getCameraYaw()) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
	            GlStateManager.rotate(ModuleManager.perspective.getCameraPitch() + (ModuleManager.perspective.getCameraPitch() - ModuleManager.perspective.getCameraPitch()) * partialTicks, -1.0F, 0.0F, 0.0F);
	        }
	    } else if (this.mc.gameSettings.thirdPersonView > 0) {
	        double d3 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);

	        if (this.mc.gameSettings.debugCamEnable) {
	            GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
	        } else {
	            float f1 = ModuleManager.perspective.getCameraYaw();
	            float f2 = ModuleManager.perspective.getCameraPitch();

	            if (this.mc.gameSettings.thirdPersonView == 2) {
	                f2 += 180.0F;
	            }

	            double d4 = (double)(-MathHelper.sin(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
	            double d5 = (double)(MathHelper.cos(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
	            double d6 = (double)(-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d3;

	            for (int i = 0; i < 8; ++i) {
	                float f3 = (float)((i & 1) * 2 - 1);
	                float f4 = (float)((i >> 1 & 1) * 2 - 1);
	                float f5 = (float)((i >> 2 & 1) * 2 - 1);
	                f3 = f3 * 0.1F;
	                f4 = f4 * 0.1F;
	                f5 = f5 * 0.1F;
	                MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), new Vec3(d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));

	                if (movingobjectposition != null) {
	                    double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

	                    if (d7 < d3) {
	                        d3 = d7;
	                    }
	                }
	            }

	            if (this.mc.gameSettings.thirdPersonView == 2) {
	                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	            }

	            GlStateManager.rotate(ModuleManager.perspective.getCameraPitch() - f2, 1.0F, 0.0F, 0.0F);
	            GlStateManager.rotate(ModuleManager.perspective.getCameraYaw() - f1, 0.0F, 1.0F, 0.0F);
	            GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
	            GlStateManager.rotate(f1 - ModuleManager.perspective.getCameraYaw(), 0.0F, 1.0F, 0.0F);
	            GlStateManager.rotate(f2 - ModuleManager.perspective.getCameraPitch(), 1.0F, 0.0F, 0.0F);
	        }
	    } else {
	        GlStateManager.translate(0.0F, 0.0F, -0.1F);
	    }

	    if (!this.mc.gameSettings.debugCamEnable) {
	        float yaw = ModuleManager.perspective.getCameraYaw() + (ModuleManager.perspective.getCameraYaw() - ModuleManager.perspective.getCameraYaw()) * partialTicks + 180.0F;
	        float pitch = ModuleManager.perspective.getCameraPitch() + (ModuleManager.perspective.getCameraPitch() - ModuleManager.perspective.getCameraPitch()) * partialTicks;
	        float roll = 0.0F;
	        if (entity instanceof EntityAnimal) {
	            EntityAnimal entityanimal = (EntityAnimal) entity;
	            yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
	        }
	        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
	        net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup(mc.entityRenderer, entity, block, partialTicks, yaw, pitch, roll);
	        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
	        GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F);
	        GlStateManager.rotate(event.pitch, 1.0F, 0.0F, 0.0F);
	        GlStateManager.rotate(event.yaw, 0.0F, 1.0F, 0.0F);
	    }

	    GlStateManager.translate(0.0F, -f, 0.0F);
	    d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
	    d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
	    d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
	    this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
	}
	
	/**
     * @author Myzo
     * @reason Implement Reach
     */
	@Overwrite
    public void getMouseOver(float p_getMouseOver_1_) {
        Entity entity = this.mc.getRenderViewEntity();
        if(entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;

            double d0 = ModuleManager.reach.isToggled() ? ModuleManager.reach.getMaxReach() : (double) this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(ModuleManager.reach.isToggled() ? ModuleManager.reach.buildreach.get() : d0, p_getMouseOver_1_);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(p_getMouseOver_1_);
            boolean flag = false;
            if(this.mc.playerController.extendedReach()) {
                d0 = 6.0D;
                d1 = 6.0D;
            }else if(d0 > 3.0D) {
                flag = true;
            }

            if(this.mc.objectMouseOver != null) {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }

            if(ModuleManager.reach.isToggled()) {
                d1 = ModuleManager.reach.combatreach.get();

                final MovingObjectPosition movingObjectPosition = entity.rayTrace(d1, p_getMouseOver_1_);

                if(movingObjectPosition != null) d1 = movingObjectPosition.hitVec.distanceTo(vec3);
            }

            Vec3 vec31 = entity.getLook(p_getMouseOver_1_);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            this.pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith()));
            double d2 = d1;

            for (Entity entity1 : list) {
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        this.pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d3 < d2 || d2 == 0.0D) {
                        if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                this.pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            this.pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > (ModuleManager.reach.isToggled() ? ModuleManager.reach.combatreach.get() : 3.0D)) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, Objects.requireNonNull(vec33), null, new BlockPos(vec33));
            }

            if(this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
                if(this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }

            this.mc.mcProfiler.endSection();
        }
    }
	
	/**
     * @author Myzo
     * @reason Implement Perspective
     */
	@Overwrite
	public void updateCameraAndRender(float p_181560_1_, long p_181560_2_) {
	    boolean flag = Display.isActive();

	    if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
	        if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
	            this.mc.displayInGameMenu();
	        }
	    } else {
	        this.prevFrameTime = Minecraft.getSystemTime();
	    }

	    this.mc.mcProfiler.startSection("mouse");

	    if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
	        Mouse.setGrabbed(false);
	        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
	        Mouse.setGrabbed(true);
	    }

	    if (this.mc.inGameHasFocus && flag && ModuleManager.perspective.overrideMouse()) {
	        this.mc.mouseHelper.mouseXYChange();
	        float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
	        float f1 = f * f * f * 8.0F;
	        float f2 = (float) this.mc.mouseHelper.deltaX * f1;
	        float f3 = (float) this.mc.mouseHelper.deltaY * f1;
	        int i = 1;

	        if (this.mc.gameSettings.invertMouse) {
	            i = -1;
	        }

	        if (this.mc.gameSettings.smoothCamera) {
	            this.smoothCamYaw += f2;
	            this.smoothCamPitch += f3;
	            float f4 = p_181560_1_ - this.smoothCamPartialTicks;
	            this.smoothCamPartialTicks = p_181560_1_;
	            f2 = this.smoothCamFilterX * f4;
	            f3 = this.smoothCamFilterY * f4;
	            this.mc.thePlayer.setAngles(f2, f3 * (float) i);
	        } else {
	            this.smoothCamYaw = 0.0F;
	            this.smoothCamPitch = 0.0F;
	            this.mc.thePlayer.setAngles(f2, f3 * (float) i);
	        }
	    }

	    this.mc.mcProfiler.endSection();

	    if (!this.mc.skipRenderWorld) {
	    	anaglyphEnable = this.mc.gameSettings.anaglyph;
	        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
	        int i1 = scaledresolution.getScaledWidth();
	        int j1 = scaledresolution.getScaledHeight();
	        final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
	        final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
	        int i2 = this.mc.gameSettings.limitFramerate;

	        if (this.mc.theWorld != null) {
	            this.mc.mcProfiler.startSection("level");
	            int j = Math.min(Minecraft.getDebugFPS(), i2);
	            j = Math.max(j, 60);
	            long k = System.nanoTime() - p_181560_2_;
	            long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
	            this.renderWorld(p_181560_1_, System.nanoTime() + l);

	            if (OpenGlHelper.shadersSupported) {
	                this.mc.renderGlobal.renderEntityOutlineFramebuffer();

	                if (this.theShaderGroup != null && this.useShader) {
	                    GlStateManager.matrixMode(5890);
	                    GlStateManager.pushMatrix();
	                    GlStateManager.loadIdentity();
	                    this.theShaderGroup.loadShaderGroup(p_181560_1_);
	                    GlStateManager.popMatrix();
	                }

	                this.mc.getFramebuffer().bindFramebuffer(true);
	            }

	            this.renderEndNanoTime = System.nanoTime();
	            this.mc.mcProfiler.endStartSection("gui");

	            if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
	                GlStateManager.alphaFunc(516, 0.1F);
	                this.mc.ingameGUI.renderGameOverlay(p_181560_1_);
	            }

	            this.mc.mcProfiler.endSection();
	        } else {
	            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
	            GlStateManager.matrixMode(5889);
	            GlStateManager.loadIdentity();
	            GlStateManager.matrixMode(5888);
	            GlStateManager.loadIdentity();
	            this.setupOverlayRendering();
	            this.renderEndNanoTime = System.nanoTime();
	        }

	        if (this.mc.currentScreen != null) {
	            GlStateManager.clear(256);

	            try {
	                net.minecraftforge.client.ForgeHooksClient.drawScreen(this.mc.currentScreen, k1, l1, p_181560_1_);
	            } catch (Throwable throwable) {
	                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
	                CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
	                crashreportcategory.addCrashSectionCallable("Screen name", new Callable < String > () {
	                    public String call() throws Exception {
	                        return MixinEntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Mouse location", new Callable < String > () {
	                    public String call() throws Exception {
	                        return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] {
	                            Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())
	                        });
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Screen size", new Callable < String > () {
	                    public String call() throws Exception {
	                        return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {
	                            Integer.valueOf(scaledresolution.getScaledWidth()), Integer.valueOf(scaledresolution.getScaledHeight()), Integer.valueOf(MixinEntityRenderer.this.mc.displayWidth), Integer.valueOf(MixinEntityRenderer.this.mc.displayHeight), Integer.valueOf(scaledresolution.getScaleFactor())
	                        });
	                    }
	                });
	                throw new ReportedException(crashreport);
	            }
	        }
	    }
	}
}