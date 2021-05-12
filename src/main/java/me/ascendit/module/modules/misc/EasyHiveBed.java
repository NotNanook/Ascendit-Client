package me.ascendit.module.modules.misc;

import org.lwjgl.opengl.GL11;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.Render3DEvent;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;

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

@ModuleInfo(name = "EasyHiveBed", description = "Makes breaking beds from above on HiveMC easy", category = Category.MISC)
public class EasyHiveBed extends Module {
	
	private int radius = 3;
    private int counter; // makes sure that it only places blocks every 5 ticks (otherwise it wont work)

    @Override
    public void onEnable() {
        counter = 0;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (counter >= 5 && !ModuleManager.bowaimer.isToggled()) {
            counter = 0;
            for (int x = radius; x >= -radius; x--) {
                for (int y = radius; y >= -radius; y--) {
                    for (int z = radius; z >= -radius; z--) {
                        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                        final BlockPos blockAbove = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y + 1, mc.thePlayer.posZ + z);

                        if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed && mc.theWorld.getBlockState(blockAbove).getBlock().getMaterial() != Material.glass) {
                            try {
                                if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemCloth && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, EnumFacing.UP, new Vec3(0 + blockPos.getX(), -1 + blockPos.getY(), 0 + blockPos.getZ()))) {
                                    mc.thePlayer.swingItem();
                                }
                            } catch (final Exception e) {
                                continue;
                            }
                        }

                    }
                }
            }
        } else {
            counter++;
        }
    }

    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final RenderManager renderManager = mc.getRenderManager();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        for (int relX = radius; relX >= -radius; relX--) {
            for (int relY = radius; relY >= -radius; relY--) {
                for (int relZ = radius; relZ >= -radius; relZ--) {
                    final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + relX, mc.thePlayer.posY + relY, mc.thePlayer.posZ + relZ);
                    if (!(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed))
                    	return;
                    
                    final double x = blockPos.getX() - renderManager.viewerPosX;
                    final double y = blockPos.getY() - renderManager.viewerPosY;
                    final double z = blockPos.getZ() - renderManager.viewerPosZ;
                    
                    AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
                    
                    final double posX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) event.getPartialTicks();
                    final double posY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) event.getPartialTicks();
                    final double posZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double) event.getPartialTicks();
                    
                    final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    
                    aabb = block.getSelectedBoundingBox(mc.theWorld, blockPos)
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
                    
                    GL11.glColor4f(1f, 0f, 0F, 0.2F);
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