package me.ascendit.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.PacketEvent;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.util.MinecraftUtils;
import me.ascendit.util.MovementUtils;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "Freecam", description = "Allows you to move your camera freely", category = Category.RENDER)
public class Freecam extends Module {
	
	private final DoubleSetting flyspeed = new DoubleSetting("FlySpeed", 1.0, 0.1, 5.0);
	private final BoolSetting noclip = new BoolSetting("NoClip", true);
	
	private EntityOtherPlayerMP fakePlayer = null;
    private double oldX;
    private double oldY;
    private double oldZ;
    private float oldPitch;
    private float oldYaw;
    
    public Freecam() {
    	addSettings(flyspeed, noclip);
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
    	final Packet<?> packet = event.getPacket();
    	if ((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction)) {
            event.setCancelled(true);;
        }
    }

	@Override
    public void onEnable() {
		if (!ModuleManager.bowaimer.isToggled()) {
			setSilent(false);
			
			oldX = mc.thePlayer.posX;
			oldY = mc.thePlayer.posY;
			oldZ = mc.thePlayer.posZ;
			oldYaw = mc.thePlayer.rotationYaw;
			oldPitch = mc.thePlayer.rotationPitch;
			
			fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
			fakePlayer.clonePlayer(mc.thePlayer, true);
			fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
			fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
			
			mc.theWorld.addEntityToWorld(-1000, fakePlayer);
			
			if (noclip.get()) {
				mc.thePlayer.noClip = true;
			}
		} else {
			setSilent(true);
			MinecraftUtils.sendMessage("Can't enable while BowAimer is enabled");
			setToggled(false);
			return;
		}
    }
	
	@Override
    public void onDisable() {
		if (noclip.get()) {
			mc.thePlayer.noClip = false;
		}
        mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, oldYaw, oldPitch);

        mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
        fakePlayer = null;

        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		setDisplayName(getName() + " " + ChatFormatting.GRAY + flyspeed.get());
		if (!ModuleManager.bowaimer.isToggled()) {
			if (noclip.get()) {
				mc.thePlayer.noClip = true;
			}
			mc.thePlayer.fallDistance = 0;
			
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			
			if (mc.gameSettings.keyBindJump.isKeyDown())
				mc.thePlayer.motionY += flyspeed.get();
			if (mc.gameSettings.keyBindSneak.isKeyDown())
				mc.thePlayer.motionY -= flyspeed.get();
			
			if (!(mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F))) {
				return;
			}
			
			final double yaw = MovementUtils.getDirection();
			mc.thePlayer.motionX = -Math.sin(yaw) * flyspeed.get();
			mc.thePlayer.motionZ = Math.cos(yaw) * flyspeed.get();
		}
    }
}