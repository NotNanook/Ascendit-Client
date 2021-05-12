package me.ascendit.module.modules.misc;

import java.util.ArrayList;
import java.util.Collection;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.util.HotbarItems;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "InventoryTracker", description = "Keeps track of other players iventory. Use .invsee to peek into it", category = Category.MISC)
public class InvTracker extends Module {
	private ArrayList<String> playerList;
	private ArrayList<HotbarItems> playerHotbars;

	public InvTracker() {
		playerList = new ArrayList<String>();
		playerHotbars = new ArrayList<HotbarItems>();
	}

	@Override
	public void onEnable() {
		for (EntityPlayer player : mc.theWorld.playerEntities) {
			playerList.add(player.getName());
			playerHotbars.add(new HotbarItems(player.getDisplayNameString()));
		}
	}

	@Override
	public void onDisable() {
		playerList.clear();
		playerHotbars.clear();
	}

	@EventTarget
	public void onUpdate(final UpdateEvent event) {
		// get all currently online player names
		ArrayList<String> currentPlayersOnline = new ArrayList<String>();
		Collection<NetworkPlayerInfo> playerInfoMap = mc.getNetHandler().getPlayerInfoMap();
		for (NetworkPlayerInfo playerInfo : playerInfoMap) {
			currentPlayersOnline.add(playerInfo.getGameProfile().getName());
		}

		// add new players
		for (String playerName : currentPlayersOnline) {
			if (!playerList.contains(playerName)) {
				playerList.add(playerName);
				playerHotbars.add(new HotbarItems(playerName));
			}
		}

		// remove players that left or died
		for (int playerNameIndex = 0; playerNameIndex < playerList.size(); playerNameIndex++) {
			String name = playerList.get(playerNameIndex);

			if (!currentPlayersOnline.contains(name)) {
				// remove name
				playerList.remove(name);

				// remove inventory
				for (int i = 0; i < playerHotbars.size(); i++) {
					if (playerHotbars.get(i).getPlayerName() == name) {
						playerHotbars.remove(playerHotbars.get(i));
						continue;
					}
				}
			}
		}

		// get items of all online players
		for (int playerNameIndex = 0; playerNameIndex < playerList.size(); playerNameIndex++) {
			// get held item
			String name = playerList.get(playerNameIndex);
			EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);

			if (player != null) {
				ItemStack item = player.inventory.getCurrentItem();
				for (int i = 0; i < playerHotbars.size(); i++) {
					if (playerHotbars.get(i).getPlayerName() == name) {
						playerHotbars.get(i).addItem(item);
					}
				}
			}
		}
	}

	public ArrayList<String> getPlayerList() {
		return playerList;
	}

	public ArrayList<HotbarItems> getPlayerHotbars() {
		return playerHotbars;
	}
}