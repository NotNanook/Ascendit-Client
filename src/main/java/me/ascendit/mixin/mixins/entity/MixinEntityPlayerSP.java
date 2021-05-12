package me.ascendit.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.ascendit.event.events.ChatEvent;
import me.ascendit.event.events.MotionEvent;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.ModuleManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer {
	
	@Shadow
    public boolean serverSprintState;

    @Shadow
    public abstract void playSound(String name, float volume, float pitch);

    @Shadow
    public int sprintingTicksLeft;

    @Shadow
    protected int sprintToggleTimer;

    @Shadow
    public float timeInPortal;

    @Shadow
    public float prevTimeInPortal;

    @Shadow
    protected Minecraft mc;

    @Shadow
    public MovementInput movementInput;

    @Shadow
    public abstract void setSprinting(boolean sprinting);

    @Shadow
    protected abstract boolean pushOutOfBlocks(double x, double y, double z);

    @Shadow
    public abstract void sendPlayerAbilities();

    @Shadow
    public float horseJumpPower;

    @Shadow
    public int horseJumpPowerCounter;

    @Shadow
    protected abstract void sendHorseJump();

    @Shadow
    public abstract boolean isRidingHorse();

    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;

    @Shadow
    private boolean serverSneakState;

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    @Shadow
    private double lastReportedPosX;

    @Shadow
    private int positionUpdateTicks;

    @Shadow
    private double lastReportedPosY;

    @Shadow
    private double lastReportedPosZ;

    @Shadow
    private float lastReportedYaw;

    @Shadow
    private float lastReportedPitch;

    @Inject(method = "swingItem", at = @At("HEAD"), cancellable = true)
    private void swingItem(CallbackInfo callbackInfo) {
    	if (ModuleManager.noswing.isToggled()) {
    		callbackInfo.cancel();
    		if (ModuleManager.noswing.serverside.get())
    			sendQueue.addToSendQueue(new C0APacketAnimation());
    	}
    }
    
    /**
     * @author Myzo
     * @reason Call MotionEvent
     */
	@Overwrite
	public void onUpdateWalkingPlayer() {
		MotionEvent motionevent = new MotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
		motionevent.call();
		
		boolean sprinting = this.isSprinting();

		if (sprinting != this.serverSprintState) {
			if (sprinting) {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
			} else {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
			}

			this.serverSprintState = sprinting;
		}

		boolean sneaking = this.isSneaking();

		if (sneaking != this.serverSneakState) {
			if (sneaking) {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			} else {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
			}

			this.serverSneakState = sneaking;
		}

		if (this.isCurrentViewEntity()) {
			double deltaX = motionevent.getX() - this.lastReportedPosX;
			double deltaY = motionevent.getY() - this.lastReportedPosY;
			double deltaZ = motionevent.getZ() - this.lastReportedPosZ;
			double deltaYaw = (double) (motionevent.getYaw() - this.lastReportedYaw);
			double deltaPitch = (double) (motionevent.getPitch() - this.lastReportedPitch);
			boolean moved = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ > 9.0E-4D || this.positionUpdateTicks >= 20;
			boolean rotated = deltaYaw != 0.0D || deltaPitch != 0.0D;

			if (this.ridingEntity == null) {
				if (moved && rotated) {
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(motionevent.getX(), motionevent.getY(), motionevent.getZ(), motionevent.getYaw(), motionevent.getPitch(), motionevent.isOnGround()));
				} else if (moved) {
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(motionevent.getX(), motionevent.getY(), motionevent.getZ(), motionevent.isOnGround()));
				} else if (rotated) {
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(motionevent.getYaw(), motionevent.getPitch(), motionevent.isOnGround()));
				} else {
					this.sendQueue.addToSendQueue(new C03PacketPlayer(motionevent.isOnGround()));
				}
			} else {
				this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, motionevent.getYaw(), motionevent.getPitch(), motionevent.isOnGround()));
				moved = false;
			}

			++this.positionUpdateTicks;

			if (moved) {
				this.lastReportedPosX = motionevent.getX();
				this.lastReportedPosY = motionevent.getY();
				this.lastReportedPosZ = motionevent.getZ();
				this.positionUpdateTicks = 0;
			}

			if (rotated) {
				this.lastReportedYaw = motionevent.getYaw();
				this.lastReportedPitch = motionevent.getPitch();
			}
		}
	}
    
	/**
     * @author Myzo
     * @reason Call ChatEvent
     */
    @Overwrite
    public void sendChatMessage(String message) {
    	ChatEvent event = new ChatEvent(message);
    	ChatEvent.handleChat(event);
    	
    	if (event.isCancelled())
    		return;
    	
        sendQueue.addToSendQueue(new C01PacketChatMessage(event.getMessage()));
    }

    /**
     * @author Myzo
     * @reason Implement NoSlow and Sprint and call UpdateEvent
     */
	@Overwrite
    public void onLivingUpdate() {
		new UpdateEvent().call();
		
        if (sprintingTicksLeft > 0) {
            --sprintingTicksLeft;

            // noslow
            if (sprintingTicksLeft == 0 && !ModuleManager.noslow.isToggled()) {
                setSprinting(false);
            }
        }

        if (sprintToggleTimer > 0) {
            --sprintToggleTimer;
        }

        prevTimeInPortal = timeInPortal;

        if (inPortal) {
            if (mc.currentScreen != null && !mc.currentScreen.doesGuiPauseGame()) {
                mc.displayGuiScreen((GuiScreen) null);
            }

            if (timeInPortal == 0.0F) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), rand.nextFloat() * 0.4F + 0.8F));
            }

            timeInPortal += 0.0125F;

            if (timeInPortal >= 1.0F) {
                timeInPortal = 1.0F;
            }

            inPortal = false;
        } else if (isPotionActive(Potion.confusion) && getActivePotionEffect(Potion.confusion).getDuration() > 60) {
            timeInPortal += 0.006666667F;

            if (timeInPortal > 1.0F) {
                timeInPortal = 1.0F;
            }
        } else {
            if (timeInPortal > 0.0F) {
                timeInPortal -= 0.05F;
            }

            if (timeInPortal < 0.0F) {
                timeInPortal = 0.0F;
            }
        }

        if (timeUntilPortal > 0) {
            --timeUntilPortal;
        }

        boolean flag = movementInput.jump;
        boolean flag1 = movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = movementInput.moveForward >= f;
        movementInput.updatePlayerMoveState();

        // noslow
        if (isUsingItem() && !isRiding() && !ModuleManager.noslow.isToggled()) {
            movementInput.moveStrafe *= 0.2F;
            movementInput.moveForward *= 0.2F;
            sprintToggleTimer = 0;
        }

        pushOutOfBlocks(posX - (double) width * 0.35D, getEntityBoundingBox().minY + 0.5D, posZ + (double) width * 0.35D);
        pushOutOfBlocks(posX - (double) width * 0.35D, getEntityBoundingBox().minY + 0.5D, posZ - (double) width * 0.35D);
        pushOutOfBlocks(posX + (double) width * 0.35D, getEntityBoundingBox().minY + 0.5D, posZ - (double) width * 0.35D);
        pushOutOfBlocks(posX + (double) width * 0.35D, getEntityBoundingBox().minY + 0.5D, posZ + (double) width * 0.35D);
        boolean flag3 = (float) getFoodStats().getFoodLevel() > 6.0F || capabilities.allowFlying;
        
        // sprint
        if (ModuleManager.sprint.isToggled() && ModuleManager.sprint.nohunger.get()) {
        	flag3 = true;
        }

        if (onGround && !flag1 && !flag2 && movementInput.moveForward >= f && !isSprinting() && flag3 && !isUsingItem() && !isPotionActive(Potion.blindness)) {
            if (sprintToggleTimer <= 0 && !mc.gameSettings.keyBindSprint.isKeyDown()) {
                sprintToggleTimer = 7;
            } else {
                setSprinting(true);
            }
        }

        if (!isSprinting() && movementInput.moveForward >= f && flag3 && !isUsingItem() && !isPotionActive(Potion.blindness) && mc.gameSettings.keyBindSprint.isKeyDown()) {
            setSprinting(true);
        }

        if (isSprinting() && (movementInput.moveForward < f || isCollidedHorizontally || !flag3)) {
            setSprinting(false);
        }

        if (capabilities.allowFlying) {
            if (mc.playerController.isSpectatorMode()) {
                if (!capabilities.isFlying) {
                    capabilities.isFlying = true;
                    sendPlayerAbilities();
                }
            } else if (!flag && movementInput.jump) {
                if (flyToggleTimer == 0) {
                    flyToggleTimer = 7;
                } else {
                    capabilities.isFlying = !capabilities.isFlying;
                    sendPlayerAbilities();
                    flyToggleTimer = 0;
                }
            }
        }

        if (capabilities.isFlying && isCurrentViewEntity()) {
            if (movementInput.sneak) {
                motionY -= (double)(capabilities.getFlySpeed() * 3.0F);
            }

            if (movementInput.jump) {
                motionY += (double)(capabilities.getFlySpeed() * 3.0F);
            }
        }

        if (mc.thePlayer.isRidingHorse()) {
            if (horseJumpPowerCounter < 0) {
                ++horseJumpPowerCounter;

                if (horseJumpPowerCounter == 0) {
                    horseJumpPower = 0.0F;
                }
            }

            if (flag && !movementInput.jump) {
                horseJumpPowerCounter = -10;
                sendHorseJump();
            } else if (!flag && movementInput.jump) {
                horseJumpPowerCounter = 0;
                horseJumpPower = 0.0F;
            } else if (flag) {
                ++horseJumpPowerCounter;

                if (horseJumpPowerCounter < 10) {
                    horseJumpPower = (float) horseJumpPowerCounter * 0.1F;
                } else {
                    horseJumpPower = 0.8F + 2.0F / (float)(horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        } else {
            horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (onGround && capabilities.isFlying && !mc.playerController.isSpectatorMode()) {
            capabilities.isFlying = false;
            sendPlayerAbilities();
        }
    }
}