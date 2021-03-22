package me.ascendit.commands;

public class CommandHelp extends Command 
{
	public CommandHelp()
	{
		super("help", "Shows you every command syntax or just for a specific one", "Usage: .help <none/command>");
		this.registerCommand();
	}
	
	@Override
	public void onCommand(String[] args) 
	{
	}

}
