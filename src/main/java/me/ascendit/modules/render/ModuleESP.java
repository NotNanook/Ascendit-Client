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
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleESP extends Module
{
	public ModuleESP()
	{
		super("ESP", "Allows you to see players through walls", Category.RENDER);
		this.registerModule();
	}
	
	@Override
	public void onRender3d(RenderWorldLastEvent event)
	{	
		for(EntityPlayer player : mc.theWorld.playerEntities)
		{
			if(player.getName() != mc.thePlayer.getName())
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
				
		        // set up opengl
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(770, 771);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
				
				// choose color by team
				if(player.getTeam() == mc.thePlayer.getTeam())
				{
					GL11.glColor4f(1f, 1f, 1F, 0.8F);
				}
				else if(player.getTeam() != mc.thePlayer.getTeam())
				{
					GL11.glColor4f(1f, 0f, 0F, 0.8F);
				}
				
				GL11.glLineWidth(1);
				
				// outline
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
		        
		        // choose color by team
				if(player.getTeam() == mc.thePlayer.getTeam())
				{
					GL11.glColor4f(1f, 1f, 1F, 0.2F);
				}
				else if(player.getTeam() != mc.thePlayer.getTeam())
				{
					GL11.glColor4f(1f, 0f, 0F, 0.2F);
				}
				
				// fill the box
    	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);

    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();

    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();

    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();

    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    	        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    	        
    	        tessellator.draw();
				
    	        // close opengl drawing proccess
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
