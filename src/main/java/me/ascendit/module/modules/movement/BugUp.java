package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.util.FallingPlayer;

import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "BugUp", description = "Automatically teleports you back from where you fell", category = Category.MOVEMENT)
public class BugUp extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", "flyflag", "teleportback", "ongroundspoof", "teleportflag");
	public final IntSetting falldistance = new IntSetting("Falldistance", 4, 2, 20);
	
	private BlockPos detectedlocation = null;
	private float lastfound = 0F;
	private double prevX;
	private double prevY;
	private double prevZ;

	public BugUp() {
		addSettings(mode, falldistance);
	}
	
	@Override
	public void onDisable() {
		prevX = 0.0;
		prevY = 0.0;
		prevZ = 0.0;
	}
	
	@EventTarget
    public void onUpdate(final UpdateEvent event) {
		addSettingtoName(mode);
		detectedlocation = null;
		EntityPlayerSP thePlayer = mc.thePlayer;
		
		if (thePlayer.onGround && !(mc.theWorld.getBlockState(new BlockPos(thePlayer.posX, thePlayer.posY - 1.0,
				thePlayer.posZ)).getBlock() instanceof BlockAir)) {
			prevX = thePlayer.prevPosX;
			prevY = thePlayer.prevPosY;
			prevZ = thePlayer.prevPosZ;
		}
		
		if (!thePlayer.onGround && !thePlayer.isOnLadder() && !thePlayer.isInWater()) {
			FallingPlayer fallingPlayer = new FallingPlayer(
					thePlayer.posX,
					thePlayer.posY,
					thePlayer.posZ,
					thePlayer.motionX,
					thePlayer.motionY,
					thePlayer.motionZ,
					thePlayer.rotationYaw,
					thePlayer.moveStrafing,
					thePlayer.moveForward
			);
			
			detectedlocation = fallingPlayer.findCollision(60).getPos();
			
			if (detectedlocation != null && Math.abs(thePlayer.posY - detectedlocation.getY()) +
					thePlayer.fallDistance <= falldistance.get()) {
				lastfound = thePlayer.fallDistance;
			}
			
			if (thePlayer.fallDistance - lastfound > falldistance.get()) {
				switch (mode.getMode().toLowerCase()) {
					case "teleportback":
						thePlayer.setPositionAndUpdate(prevX, prevY, prevZ);
						thePlayer.fallDistance = 0F;
						thePlayer.motionY = 0.0;
					case "flyflag":
						thePlayer.motionY += 0.1;
						thePlayer.fallDistance = 0F;
					case "ongroundspoof": 
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
					case "teleportflag":
						thePlayer.setPositionAndUpdate(thePlayer.posX, thePlayer.posY + 1f, thePlayer.posZ);
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
						thePlayer.motionY = 0.1;
					
						//MovementUtils.strafe()
						thePlayer.fallDistance = 0f;
				}
			}
		}
	}
}