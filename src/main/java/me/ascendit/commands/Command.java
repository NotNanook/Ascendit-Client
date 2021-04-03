package me.ascendit.commands;

import me.ascendit.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public abstract class Command {
	
	protected String command;
	protected String description;
	protected String syntax;
	protected Minecraft mc;
	protected boolean toggled;
	
	public Command(String command, String description, String syntax)
	{
		this.command = command;
		this.description = description;
		this.syntax = syntax;
		this.mc = Minecraft.getMinecraft();
		this.toggled = false;
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
	
	public boolean isToggled()
	{
		return this.toggled;
	}
	
	public void toggle()
	{
		this.toggled = !this.toggled;
	}
	
	public void sendMessage(String msg, EnumChatFormatting color)
	{
		ChatComponentText message = new ChatComponentText(msg);
		message.getChatStyle().setColor(color);
		mc.thePlayer.addChatComponentMessage(message);
	}
	
	public void onRender2d(RenderGameOverlayEvent.Text event) {}
	
	public abstract void onCommand(String[] args);
}
