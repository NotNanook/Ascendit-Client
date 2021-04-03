package me.ascendit.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.ascendit.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui
{
	@Shadow
	public Minecraft mc;
	
	@Shadow
	public static ResourceLocation widgetsTexPath;
	
	@Shadow
	public abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_);
	
	@Overwrite
	protected void renderTooltip(ScaledResolution sr, float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f = this.zLevel;
            this.zLevel = -90.0F;
            
            // --------------Custom Start--------------------
            if(!Main.customHotbar.isEnabled())
            {
            	this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            	this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }
            else
            {
            	int middleScreen = sr.getScaledWidth() / 2;
            	GuiIngame.drawRect(0, sr.getScaledHeight() - 23, sr.getScaledWidth(), sr.getScaledHeight(), Integer.MIN_VALUE);
            	GuiIngame.drawRect(middleScreen - 91 - 1 + mc.thePlayer.inventory.currentItem * 20 + 1, sr.getScaledHeight() - 23, middleScreen - 91 - 1 + mc.thePlayer.inventory.currentItem * 20 + 22, sr.getScaledHeight() - 22 - 1 + 23, Integer.MAX_VALUE);
            }
            // --------------Custom end--------------------
            
            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j)
            {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
	
}
