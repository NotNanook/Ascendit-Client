package me.ascendit.event.events;

import me.ascendit.Ascendit;
import me.ascendit.command.Command;
import me.ascendit.command.CommandManager;
import me.ascendit.event.Event;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.setting.Setting;
import me.ascendit.util.MinecraftUtils;

// Called in MixinEntityPlayerSP
public class ChatEvent extends Event {
	
	private String message;
	
	public ChatEvent(final String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(final String message) {
		this.message = message;
	}
	
	public static void handleChat(final ChatEvent event) {
		final String[] message = event.getMessage().split(" ");
		if (message[0].startsWith(".") || message[0].startsWith(",") || message[0].startsWith("#") || message[0].startsWith("+")) {
			event.setCancelled(true);
			
			boolean foundcommand = false;
			for (final Command command: CommandManager.commands) {
				if (message[0].substring(1).equalsIgnoreCase(command.getCommand()) || command.getAliases().contains(message[0].substring(1))) {
					try {
						command.onCommand(message);
						foundcommand = true;
						return;
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			for (final Module module: ModuleManager.modules) {
				if (message[0].substring(1).equalsIgnoreCase(module.getName())) {
					foundcommand = true;
					
					if (message.length == 1) {
						String s = "";
						for (int i = 0; i < module.getSettings().size(); i++) {
							if (i == module.getSettings().size() - 1)
								s += module.getSettings().get(i).getName();
							else
								s += module.getSettings().get(i).getName() + ", ";
						}
						MinecraftUtils.sendMessage(module.getSettings().isEmpty() ? "This module has no settings" : "These are valid settings: [" + s + "]");
						return;
					}
					 
					try {
						for (final Setting setting: module.getSettings()) {
							if (message[1].equalsIgnoreCase(setting.getName())) {
								if (setting instanceof BoolSetting) {
									final BoolSetting boolsetting = (BoolSetting) setting;
									if (message.length == 2) {
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 is currently set to " + boolsetting.get());
									} else if (message[2].equalsIgnoreCase("toggle")) {
										boolsetting.toggle();
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 toggled to \2478" + boolsetting.get());
										Ascendit.saveload.save();
									} else if (message[2].equalsIgnoreCase("true")) {
										boolsetting.set(true);
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 set to \2478" + boolsetting.get());
										Ascendit.saveload.save();
									} else if (message[2].equalsIgnoreCase("false")) {
										boolsetting.set(false);
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 set to \2478" + boolsetting.get());
										Ascendit.saveload.save();
									} else {
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 should be set to true or false");
									}
									
								} else if (setting instanceof DoubleSetting) {
									final DoubleSetting doublesetting = (DoubleSetting) setting;
									if (message.length == 2) {
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 is currently set to " + doublesetting.get());
									} else {
										try {
											doublesetting.set(Double.parseDouble(message[2]));
											MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 set to \2478" + doublesetting.get());
											Ascendit.saveload.save();
										} catch (final NumberFormatException e) {
											MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 should be set to a number");
										}
									}
									
								} else if (setting instanceof FloatSetting) {
									final FloatSetting floatsetting = (FloatSetting) setting;
									if (message.length == 2) {
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 is currently set to " + floatsetting.get());
									} else {
										try {
											floatsetting.set(Float.parseFloat(message[2]));
											MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 set to \2478" + floatsetting.get());
										} catch (final NumberFormatException e) {
											MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 should be set to a number");
										}
									}
									
								} else if (setting instanceof IntSetting) {
									final IntSetting intsetting = (IntSetting) setting;
									if (message.length == 2) {
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 is currently set to " + intsetting.get());
									} else {
										try {
											intsetting.set(Integer.parseInt(message[2]));
											MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 set to \2478" + intsetting.get());
										} catch (final NumberFormatException e) {
											MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 should be set to a number");
										}
									}
									
								} else if (setting instanceof ModeSetting) {
									final ModeSetting modesetting = (ModeSetting) setting;
									if (message.length == 2) {
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 is currently set to " + modesetting.getMode().toUpperCase());
									} else if (modesetting.getModes().contains(message[2].toLowerCase())) {
										modesetting.setMode(message[2].toLowerCase());
										MinecraftUtils.sendMessage(module.getName() + " \2478" + setting.getName() + "\2477 set to \2478" + modesetting.getMode().toUpperCase());
										Ascendit.saveload.save();
									} else {
										MinecraftUtils.sendMessage("These are valid modes: " + modesetting.getModes());
									}
								}
							}
						}
						return;
					} catch (ArrayIndexOutOfBoundsException e) {}
				}
			}
			if (!foundcommand) {
				MinecraftUtils.sendMessage("Command not found. View all commands by typing .help");
				return;
			}
		}
	}
}