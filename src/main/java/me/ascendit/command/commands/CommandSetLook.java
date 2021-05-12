package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.util.MinecraftUtils;

public class CommandSetLook extends Command{

	public CommandSetLook() {
		super("SetLook", ".setlook <yaw> <pitch>");
	}

	@Override
	public void onCommand(String[] args) {
		if (args.length != 3) {
			printSyntax();
			return;
		}
		try {
			mc.thePlayer.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, Float.parseFloat(args[1]), Float.parseFloat(args[2]));
			MinecraftUtils.sendMessage("Set yaw and pitch to " + "(\2478" + mc.thePlayer.rotationYaw + " " + mc.thePlayer.rotationPitch + "\2477)");
		} catch (NumberFormatException e) {
			printSyntax();
			return;
		}
	}
}