package me.ascendit.commands;

import me.ascendit.Main;

public abstract class Command {
	
	protected String command;
	protected String description;
	
	public Command(String command, String description)
	{
		this.command = command;
		this.description = description;
	}
	
	public void registerCommand()
	{
		Main.commands.add(this);
	}
	
	public String getCommand()
	{
		return this.command;
	}

	public abstract void onCommand(String[] args);
}
