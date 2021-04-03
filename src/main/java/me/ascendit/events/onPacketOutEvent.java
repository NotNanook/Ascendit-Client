package me.ascendit.events;

import me.ascendit.Main;
import me.ascendit.commands.Command;
import me.ascendit.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onPacketOutEvent {
	
	@SubscribeEvent
	public void outgoing(PacketEvent.Outgoing event) 
	{
		for(Module module : Main.modules.moduleList)
		{
			if(module.isEnabled())
			{
				module.onPacketOut(event);
			}
		}
		
		Packet<?> packet = event.getPacket();
		
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
				
				for (Module module: Main.modules.moduleList) {
                    if (message[0].substring(1).equalsIgnoreCase(module.getName())) {
                        if (message[1].equalsIgnoreCase("mode")) {
                        	String inputMode = message[2].toLowerCase();
                        	if(module.getModes().contains(inputMode))
                        	{
                        		module.setMode(inputMode);
                                module.sendMessage(module.getName() + " mode set to " + module.getMode().toUpperCase(), EnumChatFormatting.GREEN);
                        	}
                        	else
                        	{
                        		module.sendMessage("These are valid modes: " + module.getModes(), EnumChatFormatting.YELLOW);
                        	}
                        }
                    }
                }
			}
		}
	}
}
