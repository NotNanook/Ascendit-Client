package me.ascendit.modules.misc;

import java.util.ArrayList;
import java.util.Random;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ModuleFakeCheater extends Module
{
	
	private ArrayList<EntityPlayer> fakeCheatPlayers;
	private Random rd;
	
	public ModuleFakeCheater() 
	{
		super("FakeCheater", "Makes it look like some players are cheating", Category.MISC);
		this.registerModule();
		this.fakeCheatPlayers = new ArrayList<EntityPlayer>();
		this.rd = new Random();
	}

	@Override
	public void onEnable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "Fakecheater" + EnumChatFormatting.WHITE + "]: Fakecheater enabled"));
	}

	@Override
	public void onDisable() 
	{
		mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "Fakecheater" + EnumChatFormatting.WHITE + "]: Fakecheater disabled"));
	}

	@Override
	public void onTick() 
	{
		for(EntityPlayer player : this.fakeCheatPlayers)
		{
			// relink players in case they died or dc'd
			reLinkPlayers(player);
			player.setAngles(rd.nextFloat()*360, player.cameraPitch+(rd.nextFloat()*180)-90);
		}
	}

	@Override
	public void onInteract(PlayerInteractEvent event) 
	{
	}

	@Override
	public void onRender(Text event) 
	{
	}
	
	public ArrayList<EntityPlayer> getPlayerList()
	{
		return fakeCheatPlayers;
	}
	
	private void reLinkPlayers(EntityPlayer player)
	{
		for(Entity entity : mc.theWorld.loadedEntityList)
		{
			if(entity instanceof EntityPlayer)
			{
				if(player.getName().equalsIgnoreCase(entity.getName()))
				{
					this.fakeCheatPlayers.set(this.fakeCheatPlayers.indexOf(player), (EntityPlayer) entity);
				}
			}
		}
	}

}
