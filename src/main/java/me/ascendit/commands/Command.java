package me.ascendit.commands;

import me.ascendit.Main;
import net.minecraft.client.Minecraft;

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
		return syntax;
	}

	public abstract void onCommand(String[] args);
}
