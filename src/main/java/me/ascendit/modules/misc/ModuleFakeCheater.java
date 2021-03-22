package me.ascendit.modules.misc;

import java.util.ArrayList;
import java.util.Random;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

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

	public void onTick() 
	{
		for(EntityPlayer player : this.fakeCheatPlayers)
		{
			// relink players in case they died or dc'd
			reLinkPlayers(player);
			player.rotationPitch = rd.nextFloat()*360;
			player.rotationYawHead = player.cameraPitch+(rd.nextFloat()*360)-180;
		}
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
