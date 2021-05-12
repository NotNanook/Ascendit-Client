package me.ascendit.command.commands;

import me.ascendit.Ascendit;
import me.ascendit.command.Command;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.block.Block;

public class CommandXRay extends Command {

	public CommandXRay() {
		super("XRay", ".xray add/remove <block>");
	}

	@Override
	public void onCommand(final String[] args) {
		if (args[1].equalsIgnoreCase("add")) {
			if (Block.getBlockFromName(args[2]) == null) {
				MinecraftUtils.sendMessage("The block \"\2478" + args[2] + "\2477\" does not exist!");
				return;
			}
			if (!ModuleManager.xray.xrayblocks.contains(Block.getBlockFromName(args[2]))) {
				ModuleManager.xray.xrayblocks.add(Block.getBlockFromName(args[2]));
				Ascendit.saveload.save();
				MinecraftUtils.sendMessage("Added \"\2478" + args[2].toLowerCase() + "\2477\"");
			}
		} else if (args[1].equalsIgnoreCase("remove")) {
			if (Block.getBlockFromName(args[2]) == null) {
				MinecraftUtils.sendMessage("The block \"\2478" + args[2] + "\2477\" does not exist!");
				return;
			}
			if (ModuleManager.xray.xrayblocks.contains(Block.getBlockFromName(args[2]))) {
				ModuleManager.xray.xrayblocks.remove(Block.getBlockFromName(args[2]));
				Ascendit.saveload.save();
				MinecraftUtils.sendMessage("Removed \"\2478" + args[2].toLowerCase() + "\2477\"");
			} else {
				MinecraftUtils.sendMessage("This block is not in the List of XRay blocks");
				return;
			}
		}
	}
}