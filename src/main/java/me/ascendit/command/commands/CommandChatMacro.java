package me.ascendit.command.commands;

import org.lwjgl.input.Keyboard;

import me.ascendit.Ascendit;
import me.ascendit.command.ChatMacro;
import me.ascendit.command.Command;
import me.ascendit.util.MinecraftUtils;
import me.ascendit.util.StringUtils;

public class CommandChatMacro extends Command {
	
	public CommandChatMacro() {
		super("ChatMacro", ".chatmacro add/remove <message> <key>", "cm");
	}

	@Override
	public void onCommand(final String[] args) {
		if (args[1].equalsIgnoreCase("add")) {
			ChatMacro.chatmacros.add(new ChatMacro(StringUtils.toCompleteString(args, 2, args.length - 1), Keyboard.getKeyIndex(args[args.length - 1].toUpperCase())));
			MinecraftUtils.sendMessage("Message '" + StringUtils.toCompleteString(args, 2, args.length - 1) + "' bound to " + args[args.length - 1].toUpperCase());
			Ascendit.saveload.save();
			return;
		}
		if (args[1].equalsIgnoreCase("remove")) {
			for (final ChatMacro cm : ChatMacro.chatmacros) {
				if (cm.getMessage().equalsIgnoreCase(StringUtils.toCompleteString(args, 2, args.length - 1)) || cm.getKey() == Keyboard.getKeyIndex(args[2])) {
					ChatMacro.chatmacros.remove(cm);
					MinecraftUtils.sendMessage("ChatMacro " + cm.getMessage() + " on Key " + Keyboard.getKeyName(cm.getKey()) + " removed");
					Ascendit.saveload.save();
					return;
				}
			}
		}
	}
}