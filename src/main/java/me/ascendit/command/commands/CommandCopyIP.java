package me.ascendit.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import me.ascendit.command.Command;
import me.ascendit.util.MinecraftUtils;

public class CommandCopyIP extends Command {
	
	public CommandCopyIP() {
		super("CopyIP", ".copyip");
	}

	@Override
	public void onCommand(String[] args) {
		if (mc.getCurrentServerData() != null) {
			StringSelection stringselection = new StringSelection(mc.getCurrentServerData().serverIP);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, stringselection);
			MinecraftUtils.sendMessage("Server IP copied to clipboard");
		} else
			MinecraftUtils.sendMessage("You have to be on a server");
	}
}