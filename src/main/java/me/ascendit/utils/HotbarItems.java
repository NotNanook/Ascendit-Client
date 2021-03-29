package me.ascendit.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class HotbarItems 
{
	private ItemStack[] hotbarItems;
	private String playerName;
	
	public static final int SWORD_SLOT = 0;
	public static final int BOW_SLOT = 1;
	public static final int ENDERPEARL_SLOT = 2;
	public static final int LAVA_SLOT = 3;
	public static final int WATER_SLOT = 4;
	public static final int TNT_SLOT = 5;
	public static final int BLAZEROD_SLOT = 6;
	public static final int SHEEP_SLOT = 7;
	public static final int STRENGTH_SLOT = 8;
	
	private final int ENDERPEAR_ID = 368;
	private final int WATER_ID = 326;
	private final int LAVA_ID = 327;
	private final int TNT_ID = 46;
	
	public HotbarItems(String playerName)
	{
		this.playerName = playerName;
		
		hotbarItems = new ItemStack[9];
	}
	
	public void addItem(ItemStack item)
	{
		if(item == null)
		{
			return;
		}
		
		Item currentItemHeld = item.getItem();
		
		if(currentItemHeld instanceof ItemSword)
		{
			if(item != this.hotbarItems[HotbarItems.SWORD_SLOT])
			{
				this.hotbarItems[HotbarItems.SWORD_SLOT] = item;
			}
		}
		else if(currentItemHeld instanceof ItemBow)
		{
			if(item != this.hotbarItems[HotbarItems.BOW_SLOT])
			{
				this.hotbarItems[HotbarItems.BOW_SLOT] = item;
			}
		}
		else if(currentItemHeld == Item.getItemById(this.ENDERPEAR_ID))
		{
			this.hotbarItems[HotbarItems.ENDERPEARL_SLOT] = item;
		}
		else if(currentItemHeld == Item.getItemById(this.LAVA_ID))
		{
			this.hotbarItems[HotbarItems.LAVA_SLOT] = item;
		}
		else if(currentItemHeld == Item.getItemById(this.WATER_ID))
		{
			this.hotbarItems[HotbarItems.WATER_SLOT] = item;
		}
		else if(currentItemHeld == Item.getItemById(this.TNT_ID))
		{
			this.hotbarItems[HotbarItems.TNT_SLOT] = item;
		}
	}
	
	public ItemStack[] getHotbarItems()
	{
		return this.hotbarItems;
	}
	
	public String getPlayerName()
	{
		return this.playerName;
	}
}
