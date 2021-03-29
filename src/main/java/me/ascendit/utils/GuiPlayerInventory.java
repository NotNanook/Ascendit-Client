package me.ascendit.utils;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;

public class GuiPlayerInventory extends GuiInventory
{
	public GuiPlayerInventory(EntityPlayer player) 
	{
		super(player);
	}
	
	@Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) 
	{
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) 
    {
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
    }
}
