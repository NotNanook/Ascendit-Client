package me.ascendit.modules.misc;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.input.Keyboard;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import me.ascendit.utils.HotbarItems;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ModuleInventoryTracker extends Module
{
	private ArrayList<String> playerList;
	private ArrayList<HotbarItems> playerHotbars;
	private GuiInventory screenToOpen;
	
	public ModuleInventoryTracker()
	{
		super("InventoryTracker", "Keeps track of other players iventory. Use .invsee to peek into it", Category.MISC);
		this.registerModule();
		this.keyBind = Keyboard.KEY_I;
		
		this.playerList = new ArrayList<String>();
		this.playerHotbars = new ArrayList<HotbarItems>();
	}
	
	@Override
	public void onEnable()
	{
		for(EntityPlayer player : mc.theWorld.playerEntities)
		{
			this.playerList.add(player.getName());
			this.playerHotbars.add(new HotbarItems(player.getDisplayNameString()));
		}
	}
	
	@Override
	public void onDisable()
	{
		this.playerList.clear();
		this.playerHotbars.clear();
	}
	
	@Override
	public void onTick() 
	{	
		/*
		// check if any new players joined
		for(EntityPlayer player : mc.theWorld.playerEntities)
		{
			if(!this.playerList.contains(player.getName()))
			{
				this.playerList.add(player.getName());
				this.playerHotbars.add(new HotbarItems(player.getDisplayNameString()));
			}
		}*/
		
		// get all currently online player names
		ArrayList<String> currentPlayersOnline = new ArrayList<String>();
		Collection<NetworkPlayerInfo> playerInfoMap = mc.getNetHandler().getPlayerInfoMap();
		for(NetworkPlayerInfo playerInfo : playerInfoMap)
		{
			currentPlayersOnline.add(playerInfo.getGameProfile().getName());
		}
		
		// add new players 
		for(String playerName : currentPlayersOnline)
		{
			if(!this.playerList.contains(playerName))
			{
				this.playerList.add(playerName);
				this.playerHotbars.add(new HotbarItems(playerName));
			}
		}
		
		// remove players that left or died
		for(int playerNameIndex = 0; playerNameIndex < this.playerList.size(); playerNameIndex++)
		{
			String name = this.playerList.get(playerNameIndex);
			
			if(!currentPlayersOnline.contains(name))
			{
				// remove name
				playerList.remove(name);
				
				// remove inventory
				for(int i = 0; i < this.playerHotbars.size(); i++)
				{
					if(this.playerHotbars.get(i).getPlayerName() == name)
					{
						this.playerHotbars.remove(this.playerHotbars.get(i));
						continue;
					}
				}
			}
		}
		
		// get items of all online players
		for(int playerNameIndex = 0; playerNameIndex < this.playerList.size(); playerNameIndex++)
		{
			// get held item
			String name = this.playerList.get(playerNameIndex);
			EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);
			
			if(player != null)
			{
				ItemStack item = player.inventory.getCurrentItem();
				for(int i = 0; i < this.playerHotbars.size(); i++)
				{
					if(this.playerHotbars.get(i).getPlayerName() == name)
					{
						this.playerHotbars.get(i).addItem(item);
					}
				}
			}
		}
	}
	
	@Override
	public void onRender2d(RenderGameOverlayEvent.Text event)
	{
		if(this.screenToOpen != null && mc.currentScreen == null)
		{
			mc.displayGuiScreen(this.screenToOpen);
			this.setGui(null);
		}
	}
	
	public ArrayList<String> getPlayerList()
	{
		return this.playerList;
	}
	
	public ArrayList<HotbarItems> getPlayerHotbars()
	{
		return this.playerHotbars;
	}
	
	public void setGui(GuiInventory gui)
	{
		this.screenToOpen = gui;
	}
}
