package me.ascendit.module.modules.player;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.PacketEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

@ModuleInfo(name = "MoreCarry", description = "Allows you to store items in your crafting slots", category = Category.PLAYER)
public class MoreCarry extends Module {
	@EventTarget
    public void onPacket(final PacketEvent event) {
		final Packet<?> packet = event.getPacket();
		if ((packet instanceof C0DPacketCloseWindow)) {
        	event.setCancelled(true);
        }
	}
}