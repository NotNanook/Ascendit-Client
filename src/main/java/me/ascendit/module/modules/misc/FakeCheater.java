package me.ascendit.module.modules.misc;

import java.util.ArrayList;
import java.util.Random;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "FakeCheater", description = "Makes it look like some players are cheating", category = Category.MISC)
public class FakeCheater extends Module {
	
    private final ArrayList<EntityPlayer> fakeCheatPlayers = new ArrayList<EntityPlayer>();
    private final Random rd = new Random();

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (final EntityPlayer player: fakeCheatPlayers) {
            // relink players in case they died or dc'd
            reLinkPlayers(player);
            player.rotationPitch = rd.nextFloat()*360;
			player.rotationYawHead = player.cameraPitch+(rd.nextFloat()*360)-180;
        }
    }

    public ArrayList <EntityPlayer> getPlayerList() {
        return fakeCheatPlayers;
    }

    private void reLinkPlayers(final EntityPlayer player) {
        for (final Entity entity: mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                if (player.getName().equalsIgnoreCase(entity.getName())) {
                    fakeCheatPlayers.set(fakeCheatPlayers.indexOf(player), (EntityPlayer) entity);
                }
            }
        }
    }
}