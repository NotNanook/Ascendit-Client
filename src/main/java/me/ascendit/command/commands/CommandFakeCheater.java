package me.ascendit.command.commands;

import me.ascendit.command.Command;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CommandFakeCheater extends Command {
    
	public CommandFakeCheater() {
        super("FakeCheater", ".fakecheater <add/remove/clear> <name>");
    }

    @Override
	public void onCommand(final String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                // clear list
            	ModuleManager.fakecheater.getPlayerList().clear();
            	MinecraftUtils.sendMessage("Cleared fake cheater list");
                return;
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("add")) {
                // add player to list
                for (final Entity entity: Minecraft.getMinecraft().theWorld.loadedEntityList) {
                    if (entity instanceof EntityPlayer) {
                        final EntityPlayer player = (EntityPlayer) entity;
                        if (player.getName().equalsIgnoreCase(args[2]) && !ModuleManager.fakecheater.getPlayerList().contains(player)) {
                        	ModuleManager.fakecheater.getPlayerList().add(player);
                        	MinecraftUtils.sendMessage("Added player \2478" + player.getName());
                            return;
                        }
                    }
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                // remove player from list
                for (final EntityPlayer player: ModuleManager.fakecheater.getPlayerList()) {
                    if (player.getName().equalsIgnoreCase(args[2]) && ModuleManager.fakecheater.getPlayerList().contains(player)) {
                    	ModuleManager.fakecheater.getPlayerList().remove(player);
                    	MinecraftUtils.sendMessage("Removed player \2478" + player.getName());
                        return;
                    }
                }
            } else {
            	printSyntax();
            }
        } else {
        	printSyntax();
            return;
        }
    }
}