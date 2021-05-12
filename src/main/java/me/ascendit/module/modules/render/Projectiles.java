package me.ascendit.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.Render3DEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.util.ColorUtils;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Projectiles", description = "Shows you where projectiles will land", category = Category.RENDER)
public class Projectiles extends Module {

	public final ModeSetting color = new ModeSetting("Color", "custom", "bowpower", "rainbow");
	public final IntSetting red = new IntSetting("Red", 0, 0, 255);
	public final IntSetting green = new IntSetting("Green", 160, 0, 255);
	public final IntSetting blue = new IntSetting("Blue", 255, 0, 255);

	public Projectiles() {
		addSettings(color, red, green, blue);
	}
	
	@EventTarget
	public void onRender3D(final Render3DEvent event) {
		addSettingtoName(color);
		
		if (mc.thePlayer.getHeldItem() == null)
			return;
		
		EntityPlayerSP thePlayer = mc.thePlayer;
		WorldClient theWorld = mc.theWorld;
		
		ItemStack heldItem = thePlayer.getHeldItem();
		Item item = heldItem.getItem();
		RenderManager renderManager = mc.getRenderManager();
		
		boolean isBow = false;
		float motionFactor = 1.5F;
		float motionSlowdown = 0.99F;
		float gravity = 0.0F;
		float size = 0.0F;
		
		// Check items
		if (item instanceof ItemBow) {
			if (!thePlayer.isUsingItem())
				return;
			
			isBow = true;
			gravity = 0.05F;
			size = 0.3F;
			
			// Calculate power of bow
			float power = thePlayer.getItemInUseDuration() / 20.0F;
			power = (power * power + power * 2.0F) / 3.0F;
			if (power < 0.1F)
				return;
			
			if (power > 1.0F)
				power = 1.0F;
			
			motionFactor = power * 3.0F;
		} else if (item instanceof ItemFishingRod) {
			gravity = 0.04F;
			size = 0.25F;
			motionSlowdown = 0.92F;
		} else if (item instanceof ItemPotion && ItemPotion.isSplash(heldItem.getMetadata())) {
			gravity = 0.05F;
			size = 0.25F;
			motionFactor = 0.5F;
		} else {
			if (!(item instanceof ItemSnowball) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemEgg))
				return;
			
			gravity = 0.03F;
			size = 0.25F;
		}
		
		// Yaw and pitch of player
		float yaw = thePlayer.rotationYaw;
		float pitch = thePlayer.rotationPitch;
		
		float yawRadians = yaw / 180.0F * (float) Math.PI;
		float pitchRadians = pitch / 180.0F * (float) Math.PI;
		
		// Positions
		double posX = renderManager.viewerPosX - ((float) Math.cos(yawRadians) * 0.16F);
		double posY = renderManager.viewerPosY + thePlayer.getEyeHeight() - 0.10000000149011612D;
		double posZ = renderManager.viewerPosZ - ((float) Math.sin(yawRadians) * 0.16F);
		
		// Motions
		double motionX = (-((float) Math.sin(yawRadians)) * (float) Math.cos(pitchRadians)) * (isBow ? 1.0D : 0.4D);
		double motionY = -((float) Math.sin((pitch + ((item instanceof ItemPotion) ? -20 : 0)) / 180.0F * 3.1415927F)) * (isBow ? 1.0D : 0.4D);
		double motionZ = ((float) Math.cos(yawRadians) * (float) Math.cos(pitchRadians)) * (isBow ? 1.0D : 0.4D);
		double distance = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
		
		motionX /= distance;
		motionY /= distance;
		motionZ /= distance;
		motionX *= motionFactor;
		motionY *= motionFactor;
		motionZ *= motionFactor;
		
		// Landing
		MovingObjectPosition landingPosition = null;
		boolean hasLanded = false;
		boolean hitEntity = false;
		
		// Drawing
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		
		// Choose color
		switch (color.getMode().toLowerCase()) {
			case "rainbow":
				ColorUtils.glColor(ColorUtils.getRainbow());
				break;
			case "custom":
				ColorUtils.glColor(new Color(red.get(), green.get(), blue.get()));
				break;
			case "bowpower":
				ColorUtils.glColor(interpolateHSB(Color.RED, Color.GREEN, motionFactor / 30 * 10));
				break;
		}
		GL11.glLineWidth(2.0F);
		
		worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		
		while (!hasLanded && posY > 0.0D) {
			// Set pos before and after
			Vec3 posBefore = new Vec3(posX, posY, posZ);
			Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
			
			// Get landing position
			landingPosition = theWorld.rayTraceBlocks(posBefore, posAfter, false, true, false);
			
			// Set pos before and after
			posBefore = new Vec3(posX, posY, posZ);
			posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
			
			// Check if arrow is landing
			if (landingPosition != null) {
				hasLanded = true;
				posAfter = new Vec3(landingPosition.hitVec.xCoord, landingPosition.hitVec.yCoord, landingPosition.hitVec.zCoord);
			}
			
			// Set arrow box
			AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size,posY - size, posZ - size, posX + size, posY + size, posZ + size).addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D);
			
			int chunkMinX = (int) Math.floor((arrowBox.minX - 2.0D) / 16.0D);
			int chunkMaxX = (int) Math.floor((arrowBox.maxX + 2.0D) / 16.0D);
			int chunkMinZ = (int) Math.floor((arrowBox.minZ - 2.0D) / 16.0D);
			int chunkMaxZ = (int) Math.floor((arrowBox.maxZ + 2.0D) / 16.0D);
			
			// Check which entities colliding with the arrow
			List<Entity> collidedEntities = new ArrayList<Entity>();
			
			// This block of code is equivalent to the in-Range function in Kotlin
			int i = chunkMinX;
			int j = chunkMaxX;
			
			if (i <= j)
				while (true) {
					int k = chunkMinZ, m = chunkMaxZ;
					if (k <= m)
						while (true) {
							theWorld.getChunkFromChunkCoords(i, k).getEntitiesWithinAABBForEntity(thePlayer,arrowBox, collidedEntities, null);
							if (k != m) {
								k++;
								continue;
							}
							break;
						}
					if (i != j) {
						i++;
						continue;
					}
					break;
				}
			
			// Check all possible entities
			for (Entity possibleEntity : collidedEntities) {
				if (possibleEntity.canBeCollidedWith() && (possibleEntity != thePlayer)) {
					AxisAlignedBB possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox().expand(size, size, size);
					if (possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter) != null) {
						MovingObjectPosition possibleEntityLanding = possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter);
						
						hitEntity = true;
						hasLanded = true;
						landingPosition = possibleEntityLanding;
						
						continue;
					}
					possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter);
				}
			}
			
			// Affect motions of arrow
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			
			IBlockState blockState = theWorld.getBlockState(new BlockPos(posX, posY, posZ));
			
			// Check if next position is water
			if (blockState.getBlock().getMaterial() == Material.water) {
				// Update motion
				motionX *= 0.6D;
				motionY *= 0.6D;
				motionZ *= 0.6D;
			} else { // Update motion
				motionX *= motionSlowdown;
				motionY *= motionSlowdown;
				motionZ *= motionSlowdown;
			}
			
			motionY -= gravity;
			
			// Draw path
			worldRenderer.pos(posX - renderManager.viewerPosX, posY - renderManager.viewerPosY, posZ - renderManager.viewerPosZ).endVertex();
		}
		
		// End the rendering of the path
		tessellator.draw();
		GL11.glPushMatrix();
		GL11.glTranslated(posX - renderManager.viewerPosX, posY - renderManager.viewerPosY, posZ - renderManager.viewerPosZ);
		
		if (landingPosition != null) {
			// Switch rotation of hit cylinder of the hit axis
			switch (landingPosition.sideHit.getAxis().ordinal()) {
				case 0:
					GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
					break;
				case 2:
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
					break;
			}
			
			// Check if hitting a entity
			if (hitEntity) ColorUtils.glColor(new Color(255, 0, 0, 150));
		}
		
		// Rendering hit cylinder
		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
		
		Cylinder cylinder = new Cylinder();
		cylinder.setDrawStyle(100011);
		cylinder.draw(0.2F, 0.0F, 0.0F, 60, 1);
		
		// Close Open-GL drawing process
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		return;
	}

	public final Color interpolateHSB(Color startColor, Color endColor, float process) {
		float[] startHSB = Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), null);
		float[] endHSB = Color.RGBtoHSB(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), null);
		
		float brightness = (startHSB[2] + endHSB[2]) / 2;
		float saturation = (startHSB[1] + endHSB[1]) / 2;
		
		float hueMax = (startHSB[0] > endHSB[0]) ? startHSB[0] : endHSB[0];
		float hueMin = (startHSB[0] > endHSB[0]) ? endHSB[0] : startHSB[0];
		float hue = (hueMax - hueMin) * process + hueMin;
		
		return Color.getHSBColor(hue, saturation, brightness);
	}
}