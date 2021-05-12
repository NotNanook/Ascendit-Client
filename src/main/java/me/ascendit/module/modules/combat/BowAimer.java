package me.ascendit.module.modules.combat;

import java.util.ArrayList;
import java.util.List;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.PacketEvent;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;
import me.ascendit.util.MovementUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleInfo(name = "BowAimer", description = "Aims onto a specific block for you", category = Category.COMBAT)
public class BowAimer extends Module {
	
    public static EntityOtherPlayerMP fakePlayer = null;
    public static double oldX;
    public static double oldY;
    public static double oldZ;

    @EventTarget
    public void onPacket(final PacketEvent event) {
    	final Packet<?> packet = event.getPacket();
    	if ((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction)) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
    		// Make player able to fly and phase through blocks
    		mc.thePlayer.noClip = true;
    		mc.thePlayer.fallDistance = 0;
    		
    		// Reset their motion
    		mc.thePlayer.motionY = 0;
    		mc.thePlayer.motionX = 0;
    		mc.thePlayer.motionZ = 0;
    		
    		// Handle flying up and down
    		if (mc.gameSettings.keyBindJump.isKeyDown())
    			mc.thePlayer.motionY += 0.8;
    		if (mc.gameSettings.keyBindSneak.isKeyDown())
    			mc.thePlayer.motionY -= 0.8;
    		
    		if (!(mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F))) {
    			return;
    		}
    		
    		// Calculate motionX and motionZ
    		final double yaw = MovementUtils.getDirection();
    		mc.thePlayer.motionX = -Math.sin(yaw) * 0.8;
    		mc.thePlayer.motionZ = Math.cos(yaw) * 0.8;
    }

    @Override
    public void onEnable() {
    	MinecraftForge.EVENT_BUS.register(this);
    	if (!ModuleManager.freecam.isToggled()) {
    		setSilent(false);
			// Get old player position
			oldX = mc.thePlayer.posX;
			oldY = mc.thePlayer.posY;
			oldZ = mc.thePlayer.posZ;

			// Create a fake player and make him a "copy" of the real player
			fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
			fakePlayer.clonePlayer(mc.thePlayer, true);
			fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
			fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);

			// Add him to the world
			mc.theWorld.addEntityToWorld(-1000, fakePlayer);

			// Give the player no-clip so he can fly and phase thru blocks
			mc.thePlayer.noClip = true;

		} else {
			setSilent(true);
			MinecraftUtils.sendMessage("Can't enable while Freecam is enabled");
			setToggled(false);
			return;
		}
    }

    @Override
    public void onDisable() {
        // Update player angle
        mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);

        // Remove fake player
        mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
        fakePlayer = null;

        // Reset player motion
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }

    @SubscribeEvent
    public void onInteract(final PlayerInteractEvent event) {
    	final BlockPos pos = event.pos;
		final World world = event.entityPlayer.getEntityWorld();
        if (isToggled() && !(pos == null) && !world.isAirBlock(pos)) {
            toggle();

            mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.motionZ = 0;

            final double dx = oldX - pos.getX();
            final double dz = oldZ - pos.getZ();

            // calculate yaw with trigonometry
            double nYaw = 0;

            if (dx <= 0 && dz <= 0) {
                nYaw = -Math.toDegrees(Math.atan(dx / dz));
            } else if (dx >= 0 && dz <= 0) {
                nYaw = Math.toDegrees(-Math.atan(dx / dz));
            } else if (dx <= 0 && dz >= 0) {
                nYaw = Math.toDegrees(Math.atan(dz / dx)) - 90;
            } else if (dx >= 0 && dz >= 0) {
                nYaw = Math.toDegrees(Math.atan(dz / dx)) + 90;
            }

            // set play yaw
            mc.thePlayer.rotationYaw = (float) nYaw;

            // if couldn't find pitch, then write that to the player
            if (calculatePitch(mc, nYaw, pos)) {
                MinecraftUtils.sendMessage("Found angle " + "(\2478" + String.valueOf(mc.thePlayer.rotationYaw) + " " + String.valueOf(mc.thePlayer.rotationPitch) + "\2477)");
            } else {
            	MinecraftUtils.sendMessage("No valid angle found");
            }
        }
    }

    private boolean calculatePitch(final Minecraft mc, final double nYaw, final BlockPos pos) {
        // fully charged bow
        final double powerMod = 3;
        final float yaw = (float) nYaw;

        final List<Vector3d> vlist = new ArrayList<Vector3d>();

        for (double pitch = -90.0; pitch <= 90.0; pitch += 0.1) {

            pitch = Math.round(pitch * 10) / 10.0;

            double posX = mc.thePlayer.posX;
            double posY = mc.thePlayer.eyeHeight + mc.thePlayer.posY;
            double posZ = mc.thePlayer.posZ;

            posX = posX - Math.cos(yaw / 180.0F * Math.PI) * 0.16F;
            posY = posY - 0.1000000014901161F;
            posZ = posZ - Math.sin(yaw / 180.0F * Math.PI) * 0.16F;


            double motX = (-Math.sin(yaw / 180.0F * Math.PI) * Math.cos(pitch / 180.0F * Math.PI));
            double motZ = (Math.cos(yaw / 180.0F * Math.PI) * Math.cos(pitch / 180.0F * Math.PI));
            double motY = (-Math.sin(pitch / 180.0F * Math.PI));

            final double f2 = Math.sqrt(motX * motX + motY * motY + motZ * motZ);
            motX /= f2;
            motY /= f2;
            motZ /= f2;

            motX *= powerMod;
            motY *= powerMod;
            motZ *= powerMod;

            double y = posY + motY;
            double totalDistance = 0.0;
            while (y > 0 && totalDistance < 400) {
                motY *= 0.99D;
                motX *= 0.99D;
                motZ *= 0.99D;
                motY -= 0.05F;

                final Vector3d vec = new Vector3d();
                vec.x = motX;
                vec.y = motY;
                vec.z = motZ;

                vlist.add(vec);
                y += motY;
                totalDistance += Math.sqrt(Math.pow(motX, 2) + Math.pow(motY, 2) + Math.pow(motZ, 2));
            }

            // get all vectors and add them to player look vector to see if no block are in the way
            for (final Vector3d vector: vlist) {
                posX += vector.x;
                posY += vector.y;
                posZ += vector.z;

                final BlockPos loc = new BlockPos(posX, posY, posZ);

                // check if arrow landed in the area
                if ((loc.getX() == pos.getX() || loc.getX() == pos.getX() - 1 || loc.getX() == pos.getX() + 1) &&
                    (loc.getZ() == pos.getZ() || loc.getZ() == pos.getZ() - 1 || loc.getZ() == pos.getZ() + 1) &&
                    (loc.getY() == pos.getY() || loc.getY() == pos.getY() - 1 || loc.getY() == pos.getY() + 1)) {
                    // set the player pitch
                    mc.thePlayer.rotationPitch = (float) pitch;
                    return true;
                } else if (mc.theWorld.getBlockState(loc).getBlock() != Blocks.air) {
                    break;
                }
            }
            vlist.clear();

        }
        return false;
    }
}