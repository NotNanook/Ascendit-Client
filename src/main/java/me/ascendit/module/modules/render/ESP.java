package me.ascendit.module.modules.render;

import org.lwjgl.opengl.GL11;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.Render3DEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.util.ColorUtils;
import me.ascendit.util.RenderUtils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

@ModuleInfo(name = "ESP", description = "Allows you to see players through walls", category = Category.RENDER)
public class ESP extends Module {

	public final ModeSetting mode = new ModeSetting("Mode", "box", "outline", "shaderoutline", "wireframe");
	public final ModeSetting targetmode = new ModeSetting("Targets", "players", "entities");
	public final IntSetting boxwidth = new IntSetting("OutlineWidth", 2, 1, 10);
	public final IntSetting shaderoutlinewidth = new IntSetting("ShaderWidth", 5, 1, 10);
	public final IntSetting wireframewidth = new IntSetting("WireframeWidth", 5, 1, 10);
	private final ModeSetting color = new ModeSetting("Color", "default", "custom", "rainbow");
	private final IntSetting red = new IntSetting("Red", 0, 0, 255);
	private final IntSetting green = new IntSetting("Green", 40, 0, 255);
	private final IntSetting blue = new IntSetting("Blue", 255, 0, 255);

	public ESP() {
		addSettings(mode, targetmode, boxwidth, shaderoutlinewidth, wireframewidth, color, red, green, blue);
	}

	public float[] getColor() {
		if (ModuleManager.teams.isToggled() && ModuleManager.teams.esp.get()) {
			if (mc.thePlayer.getTeam() == mc.thePlayer.getTeam()) {
				if (color.getMode().equalsIgnoreCase("rainbow"))
					return new float[] { ColorUtils.getRainbow3F()[0], ColorUtils.getRainbow3F()[1],
							ColorUtils.getRainbow3F()[2], 1F };
				if (color.getMode().equalsIgnoreCase("custom"))
					return new float[] { red.get() / 255F, green.get() / 255F, blue.get() / 255F, 1F };
				return new float[] { 1F, 1F, 1F, 1F };
			}
			return new float[] { 1F, 0.1F, 0.1F, 1F };
		}

		if (color.getMode().equalsIgnoreCase("rainbow"))
			return new float[] { ColorUtils.getRainbow3F()[0], ColorUtils.getRainbow3F()[1],
					ColorUtils.getRainbow3F()[2], 1F };
		if (color.getMode().equalsIgnoreCase("custom"))
			return new float[] { red.get() / 255F, green.get() / 255F, blue.get() / 255F, 1F };
		return new float[] { 1F, 1F, 1F, 1F };
	}

	@EventTarget
	public void onRender3D(final Render3DEvent event) {
		addSettingtoName(mode);
		for (final Entity entity : mc.theWorld.getLoadedEntityList()) {
			if (!(entity instanceof EntityLivingBase)
					|| (targetmode.getMode().equalsIgnoreCase("players") && !(entity instanceof EntityPlayer))
					|| entity.getName() == mc.thePlayer.getName())
				continue;

			final RenderManager renderManager = mc.getRenderManager();

			final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()
					- renderManager.viewerPosX;
			final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()
					- renderManager.viewerPosY;
			final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()
					- renderManager.viewerPosZ;

			final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
			final AxisAlignedBB aabb = new AxisAlignedBB(entityBox.minX - entity.posX + x - 0.05D,
					entityBox.minY - entity.posY + y, entityBox.minZ - entity.posZ + z - 0.05D,
					entityBox.maxX - entity.posX + x + 0.05D, entityBox.maxY - entity.posY + y + 0.15D,
					entityBox.maxZ - entity.posZ + z + 0.05D);

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);

			GL11.glColor4f(getColor()[0], getColor()[1], getColor()[2], getColor()[3]);

			if (((EntityLivingBase) entity).hurtTime > 0)
				GL11.glColor4f(1F, 0F, 0F, 1F);

			GL11.glLineWidth(boxwidth.get());

			if (mode.getMode().equalsIgnoreCase("outline") || mode.getMode().equalsIgnoreCase("box"))
				RenderUtils.drawSelectionBoundingBox(aabb);

			if (mode.getMode().equalsIgnoreCase("box")) {
				if (color.getMode().equalsIgnoreCase("rainbow"))
					GL11.glColor4f(ColorUtils.getRainbow3F()[0], ColorUtils.getRainbow3F()[1],
							ColorUtils.getRainbow3F()[2], 0.3F);
				else if (color.getMode().equalsIgnoreCase("custom"))
					GL11.glColor4f(red.get() / 255F, green.get() / 255F, blue.get() / 255F, 0.3F);
				else
					GL11.glColor4f(1F, 1F, 1F, 0.2F);

				if (((EntityLivingBase) entity).hurtTime > 0)
					GL11.glColor4f(1F, 0F, 0F, 0.2F);

				RenderUtils.drawFilledBox(aabb);
			}

			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}
}