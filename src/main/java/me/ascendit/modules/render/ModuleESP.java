package me.ascendit.modules.render;

import org.lwjgl.opengl.GL11;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModuleESP extends Module
{
	public ModuleESP()
	{
		super("ESP", "Allows you to see players through walls", Category.RENDER);
		this.registerModule();
	}
	
	@Override
	public void onEnable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "ESP" + EnumChatFormatting.WHITE + "]: ESP enabled"));
	}

	@Override
	public void onDisable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "ESP" + EnumChatFormatting.WHITE + "]: ESP disabled"));
	}

	@Override
	public void onTick() 
	{
	}

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}
	
	@Override
	public void onRender2d(RenderGameOverlayEvent.Text event) 
	{
	}
	
	@Override
	public void onRender3d(RenderWorldLastEvent event)
	{	
		for(EntityPlayer player : mc.theWorld.playerEntities)
		{
			if(player.getName() == mc.thePlayer.getName())
			{
				RenderManager renderManager = mc.getRenderManager();
				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldRenderer = tessellator.getWorldRenderer();

				double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks - renderManager.viewerPosX;
		        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks - renderManager.viewerPosY;
		        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks - renderManager.viewerPosZ;

		        AxisAlignedBB entityBox = player.getEntityBoundingBox();
		        AxisAlignedBB aabb = new AxisAlignedBB
        		(
	                entityBox.minX - player.posX + x - 0.05D,
	                entityBox.minY - player.posY + y,
	                entityBox.minZ - player.posZ + z - 0.05D,
	                entityBox.maxX - player.posX + x + 0.05D,
	                entityBox.maxY - player.posY + y + 0.15D,
	                entityBox.maxZ - player.posZ + z + 0.05D
		        );
				
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(770, 771);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
				GL11.glColor4f(1f, 1f, 1F, 1F);
				GL11.glLineWidth(2);
				
				worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

		        // Lower Rectangle
				worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
				worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
				worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
				worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
				worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();

		        // Upper Rectangle
				worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
				worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
				worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
				worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
				worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

		        // Upper Rectangle
				worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
				worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();

				worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
				worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();

				worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
		        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
		        
		        tessellator.draw();
				
				//GL11.glTranslated(0, 0, 0);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}
	}
}
