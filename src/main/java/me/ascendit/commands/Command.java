package me.ascendit.commands;

import me.ascendit.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public abstract class Command {
	
	protected String command;
	protected String description;
	protected String syntax;
	protected Minecraft mc;
	
	public Command(String command, String description, String syntax)
	{
		this.command = command;
		this.description = description;
		this.syntax = syntax;
		this.mc = Minecraft.getMinecraft();
	}
	
	public void registerCommand()
	{
		Main.commands.add(this);
	}
	
	public String getCommand()
	{
		return this.command;
	}
	
	public String getSyntax()
	{
		return this.syntax;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void sendMessage(String msg, EnumChatFormatting color)
	{
		ChatComponentText message = new ChatComponentText(msg);
		message.getChatStyle().setColor(color);
		mc.thePlayer.addChatComponentMessage(message);
	}

	public abstract void onCommand(String[] args);
}
