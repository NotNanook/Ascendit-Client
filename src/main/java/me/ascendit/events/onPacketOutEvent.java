package me.ascendit.events;

import me.ascendit.Main;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onPacketOutEvent {
	
	@SubscribeEvent
	public void outgoing(PacketEvent.Outgoing event) {
		Packet<?> packet = event.getPacket();

		if((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction) && Main.projectileAimer.isEnabled()) 
		{
			event.setCanceled(true);
		}
	}
}
