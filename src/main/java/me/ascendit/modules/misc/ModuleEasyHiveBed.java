package me.ascendit.modules.misc;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.ascendit.Main;
import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemCloth;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleEasyHiveBed extends Module{
	
	protected int radius = 3;
	protected int counter; // makes sure that it only places blocks every 5 ticks (otherwise you will get kicked for too many packets)
	
	public ModuleEasyHiveBed()
	{
		super("EasyHiveBed", "Makes breaking beds from above on HiveMC easy", Category.MISC);
		this.registerModule();
		this.keyBind = Keyboard.KEY_O;
	}
	
	@Override
	public void onEnable() 
	{
		counter = 0;
	}
	
	@Override
	public void onDisable() 
	{
	}
	
	@Override
	public void onTick() 
	{
		if(counter >= 5 && !Main.projectileAimer.isEnabled())
		{
			counter = 0;
			for(int x = radius; x >= -radius; x--)
	        {
				for(int y = radius; y >= -radius; y--)
	            {
	            	for(int z = radius; z >= -radius; z--)
	                {
	                	BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
	                	BlockPos blockAbove = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y + 1, mc.thePlayer.posZ + z);
	                	if(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed && mc.theWorld.getBlockState(blockAbove).getBlock().getMaterial() != Material.glass)
	                    {	
	                		try
	                		{
	                			if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemCloth && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, EnumFacing.UP, new Vec3(0 + blockPos.getX(), -1 + blockPos.getY(), 0 + blockPos.getZ())))
	                            {
	                                mc.thePlayer.swingItem();
	                            }
	                		} 
	                		catch(Exception e)
	                 		{
	                			continue;
	                		}
	                    }
	                }
	            }
	        }
		}
		else
		{
			counter++;
		}
	}
	
	@Override
	public void onRender3d(RenderWorldLastEvent event)
	{	
		RenderManager renderManager = mc.getRenderManager();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		for(int relX = radius; relX >= -radius; relX--)
        {
			for(int relY = radius; relY >= -radius; relY--)
            {
            	for(int relZ = radius; relZ >= -radius; relZ--)
                {
            		BlockPos blockPos = new BlockPos(mc.thePlayer.posX + relX, mc.thePlayer.posY + relY, mc.thePlayer.posZ + relZ);
            		if(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed)
            		{
            			double x = blockPos.getX() - renderManager.viewerPosX;
            	        double y = blockPos.getY() - renderManager.viewerPosY;
            	        double z = blockPos.getZ() - renderManager.viewerPosZ;
            	        
            	        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
            	        
            	        final double posX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) event.partialTicks;
            	        final double posY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) event.partialTicks;
            	        final double posZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double) event.partialTicks;
            	        
            	        Block block = mc.theWorld.getBlockState(blockPos).getBlock();
            	        
            	        axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, blockPos)
            	                .expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
            	                .offset(-posX, -posY, -posZ);
            	        
            	        GL11.glPushMatrix();
            			GL11.glEnable(GL11.GL_BLEND);
            			GL11.glBlendFunc(770, 771);
            			GL11.glDisable(GL11.GL_TEXTURE_2D);
            			GL11.glEnable(GL11.GL_LINE_SMOOTH);
            			GL11.glDisable(GL11.GL_DEPTH_TEST);
            			GL11.glDepthMask(false);
            			GL11.glColor4f(1f, 0f, 0F, 1F);
            			GL11.glLineWidth(2);
            			
            			worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

            	        // Lower Rectangle
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

            	        // Upper Rectangle
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

            	        // Upper Rectangle
            			worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

            	        tessellator.draw();
            	        
            	        GL11.glColor4f(1f, 0f, 0F, 0.2F);
            	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);

            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
            	        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
            	        
            	        tessellator.draw();
            	        
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
	}
}