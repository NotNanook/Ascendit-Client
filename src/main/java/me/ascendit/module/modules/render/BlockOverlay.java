package me.ascendit.module.modules.render;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.Render2DEvent;
import me.ascendit.event.events.Render3DEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.ui.font.FontRenderer;
import me.ascendit.util.ColorUtils;
import me.ascendit.util.RenderUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@ModuleInfo(name = "BlockOverlay", description = "Replaces the default blockoverlay with a custom one", category = Category.RENDER)
public class BlockOverlay extends Module {

	private final ModeSetting rendermode = new ModeSetting("RenderMode", "box", "outline");
	private final ModeSetting colormode = new ModeSetting("Color", "rainbow", "default", "custom");
	private final IntSetting red = new IntSetting("Red", 0, 0, 255);
	private final IntSetting green = new IntSetting("Green", 40, 0, 255);
	private final IntSetting blue = new IntSetting("Blue", 255, 0, 255);
	private final BoolSetting renderair = new BoolSetting("RenderAir", false);
	private final BoolSetting showdistance = new BoolSetting("ShowDistance", false);
	private final ModeSetting showdistancerange = new ModeSetting("Range", "200 Blocks", "Custom");
	private final IntSetting customrange = new IntSetting("CustomRange", 10, 1, 200);

	private FontRenderer fontrenderer = new FontRenderer("Comfortaa", Font.PLAIN, 20);
	
	public BlockOverlay() {
		addSettings(rendermode, colormode, red, green, blue, renderair, showdistance, showdistancerange, customrange);
	}

	@EventTarget
	public void onRender2D(final Render2DEvent event) {
		ScaledResolution sr = event.getResolution();
		
		if (getDistance() != 0 && showdistance.get()) {
			if (getDistance() >= 100) {
				if (ModuleManager.hud.customfont.get())
					fontrenderer.drawStringWithShadow(String.valueOf(getDistance()), sr.getScaledWidth() / 2 - 8, sr.getScaledHeight() / 2 + 30, -1);
				else
					mc.fontRendererObj.drawStringWithShadow(String.valueOf(getDistance()), sr.getScaledWidth() / 2 - 8, sr.getScaledHeight() / 2 + 30, -1);
			}
			else {
				if (ModuleManager.hud.customfont.get())
					fontrenderer.drawStringWithShadow(String.valueOf(getDistance()), sr.getScaledWidth() / 2 - (getDistance() >= 10 ? 5 : 2), sr.getScaledHeight() / 2 + 30, -1);
				else
					mc.fontRendererObj.drawStringWithShadow(String.valueOf(getDistance()), sr.getScaledWidth() / 2 - (getDistance() >= 10 ? 5 : 2), sr.getScaledHeight() / 2 + 30, -1);
			}
		}
	}

	@EventTarget
	public void onRender3D(final Render3DEvent event) {
		final RenderManager renderManager = mc.getRenderManager();

		if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
			return;

		BlockPos blockpos = mc.objectMouseOver.getBlockPos();
		final Block block = mc.theWorld.getBlockState(blockpos).getBlock();

		if (renderair.get() ? (false) : (block instanceof BlockAir))
			return;

		final double x = blockpos.getX() - renderManager.viewerPosX;
		final double y = blockpos.getY() - renderManager.viewerPosY;
		final double z = blockpos.getZ() - renderManager.viewerPosZ;

		AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);

		if (block != null) {

			final double posX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) event.getPartialTicks();
			final double posY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) event.getPartialTicks();
			final double posZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double) event.getPartialTicks();

			block.setBlockBoundsBasedOnState(mc.theWorld, blockpos);
			aabb = block.getSelectedBoundingBox(mc.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-posX, -posY, -posZ);
		}

		// Start drawing
		GL11.glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);

		// Choose color
		if (colormode.getMode().equalsIgnoreCase("rainbow"))
			GL11.glColor4f(ColorUtils.getRainbow3F()[0], ColorUtils.getRainbow3F()[1], ColorUtils.getRainbow3F()[2],0.2F);
		else if (colormode.getMode().equalsIgnoreCase("default"))
			GL11.glColor4f(0F, 0.3F, 1F, 0.2F);
		else if (colormode.getMode().equalsIgnoreCase("custom"))
			ColorUtils.glColor(red.get(), green.get(), blue.get(), 40);

		if (rendermode.getMode().equalsIgnoreCase("box"))
			RenderUtils.drawFilledBox(aabb);

		GL11.glLineWidth(2);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		if (colormode.getMode().equalsIgnoreCase("rainbow"))
			GL11.glColor4f(ColorUtils.getRainbow3F()[0], ColorUtils.getRainbow3F()[1], ColorUtils.getRainbow3F()[2],1F);
		else if (colormode.getMode().equalsIgnoreCase("default"))
			GL11.glColor4f(0F, 0.1F, 1F, 1F);
		else if (colormode.getMode().equalsIgnoreCase("custom"))
			ColorUtils.glColor(red.get(), green.get(), blue.get(), 255);

		if (rendermode.getMode().equalsIgnoreCase("box") || rendermode.getMode().equalsIgnoreCase("outline"))
			RenderUtils.drawSelectionBoundingBox(aabb);

		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private int getDistance() {
		MovingObjectPosition lookingAt = RenderUtils.rayTrace(200);

		if (showdistancerange.getMode().equalsIgnoreCase("Custom")) lookingAt = mc.getRenderViewEntity().rayTrace(customrange.get(), 1);

		if (lookingAt != null && lookingAt.typeOfHit == MovingObjectType.BLOCK) {
			final BlockPos block_pos = lookingAt.getBlockPos();
			final BlockPos player_pos = mc.thePlayer.getPosition();

			return (int) Math.round(Math.sqrt(player_pos.distanceSq(block_pos)));
		}
		return 0;
	}
}