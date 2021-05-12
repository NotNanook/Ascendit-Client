package me.ascendit.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.MotionEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class MovementUtils {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static String currentSpeed = "0.00";
	
	public static String getCurrentSpeed() {
		return currentSpeed;
	}
	
	public static boolean isMoving() {
		return mc.thePlayer != null && (mc.thePlayer.moveForward != 0F || mc.thePlayer.moveStrafing != 0F);
	}
	
	@EventTarget
	public void updateSpeed(final MotionEvent event) {
		if (mc.thePlayer != null) {
			double distTraveledLastTickX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double distTraveledLastTickZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			
			final DecimalFormat df = new DecimalFormat("#.##");
	        df.setRoundingMode(RoundingMode.CEILING);
	        
	        if ((distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ) == 0)
	        	MovementUtils.currentSpeed = "0.00";
	        else
	        	MovementUtils.currentSpeed = df.format((MathHelper.sqrt_double(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ) / 0.05F));
		}
	}
	
	public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}