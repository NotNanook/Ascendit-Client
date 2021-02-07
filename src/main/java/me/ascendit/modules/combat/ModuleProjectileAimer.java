package me.ascendit.modules.combat;

import me.ascendit.Main;
import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ModuleProjectileAimer extends Module
{
	
	public static EntityOtherPlayerMP fakePlayer = null;
	public static double oldX;
	public static double oldY;
	public static double oldZ;
	
	public ModuleProjectileAimer()
	{
		super("Projectile Aimer", "Aims projectiles for you onto a specific block", Category.COMBAT);
		this.registerModule();
	}
	
	@Override
	public void onTick() 
	{
		mc.thePlayer.noClip = true;
		mc.thePlayer.fallDistance = 0;

		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionZ = 0;
		if (mc.gameSettings.keyBindJump.isKeyDown())
			mc.thePlayer.motionY += 0.8;
		if (mc.gameSettings.keyBindSneak.isKeyDown())
			mc.thePlayer.motionY -= 0.8;

		if (!(mc.thePlayer != null
				&& (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F))) {
			return;
		}

		final double yaw = getDirection();
		mc.thePlayer.motionX = -Math.sin(yaw) * 0.8;
		mc.thePlayer.motionZ = Math.cos(yaw) * 0.8;
	}

	@Override
	public void onEnable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: Freecam enabled"));

		oldX = mc.thePlayer.posX;
		oldY = mc.thePlayer.posY;
		oldZ = mc.thePlayer.posZ;

		fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
		fakePlayer.clonePlayer(mc.thePlayer, true);

		fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
		fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);

		mc.theWorld.addEntityToWorld(-1000, fakePlayer);

		mc.thePlayer.noClip = true;
	}

	@Override
	public void onDisable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: Freecam disabled"));

		mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
		mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
		fakePlayer = null;
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
	}
	
	private double getDirection() 
	{
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
