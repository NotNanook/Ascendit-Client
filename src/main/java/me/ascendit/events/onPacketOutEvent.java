package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.commands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onPacketOutEvent {
	
	@SubscribeEvent
	public void outgoing(PacketEvent.Outgoing event) {
		Packet<?> packet = event.getPacket();
		Minecraft mc = Minecraft.getMinecraft();
		
		// projectile aimer freecam
		if((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction) && Main.projectileAimer.isEnabled()) 
		{
			event.setCanceled(true);
		}
		
		// chat commands
		if(packet instanceof C01PacketChatMessage)
		{
			C01PacketChatMessage messagePacket = (C01PacketChatMessage) packet;
			String[] message = messagePacket.getMessage().split(" "); 
			
			if(message[0].startsWith("."))
			{
				event.setCanceled(true);
				for(Command command : Main.commands)
				{
					if(message[0].substring(1).equalsIgnoreCase(command.getCommand()))
					{
						command.onCommand(message);
					}
				}
			}
		}
		
		if(packet instanceof S18PacketEntityTeleport)
		{
			mc.thePlayer.addChatComponentMessage(new ChatComponentText("Tp packet sent!"));
		}
	}
}
