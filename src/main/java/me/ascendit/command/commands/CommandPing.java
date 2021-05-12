package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.util.MinecraftUtils;

public class CommandPing extends Command {

	public CommandPing() {
		super("Ping", ".ping", "p");
	}

	@Override
	public void onCommand(final String[] args) {
		if (mc.thePlayer != null && mc.theWorld != null && mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null)
			MinecraftUtils.sendMessage("Your Ping is \2478" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + " \2477ms");
		else {
			MinecraftUtils.sendMessage("An Error ocurred. Please try again later");
			return;
		}
	}
}