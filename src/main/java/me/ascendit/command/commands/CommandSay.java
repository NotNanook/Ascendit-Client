package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.util.StringUtils;

import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandSay extends Command {

	public CommandSay() {
		super("Say", ".say <message>");
	}
	
	@Override
	public void onCommand(final String[] args) {
		mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(StringUtils.toCompleteString(args, 1)));
	}
}