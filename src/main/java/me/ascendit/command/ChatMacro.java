package me.ascendit.command;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.KeyEvent;

import net.minecraft.client.Minecraft;

public class ChatMacro {
	
	public static CopyOnWriteArrayList<ChatMacro> chatmacros = new CopyOnWriteArrayList<ChatMacro>();
	private String message;
	private int key;

	public ChatMacro(final String message, final int key) {
		this.message = message;
		this.key = key;
	}
	
	public ChatMacro() {}
	
	@EventTarget
	public void onKey(final KeyEvent event) {
		for (final ChatMacro chatmacro: ChatMacro.chatmacros) {
    		if (chatmacro.getKey() == Keyboard.getEventKey() && Keyboard.getEventKeyState()) {
    			Minecraft.getMinecraft().thePlayer.sendChatMessage(chatmacro.getMessage());
    		}
    	}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(final String command) {
		this.message = command;
	}

	public int getKey() {
		return key;
	}

	public void setKey(final int key) {
		this.key = key;
	}
}