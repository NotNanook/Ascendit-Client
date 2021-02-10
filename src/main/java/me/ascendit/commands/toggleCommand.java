package me.ascendit.commands;


import me.ascendit.Main;
import me.ascendit.modules.Module;

public class toggleCommand extends Command
{
	public toggleCommand()
	{
		super("t", "Toggles a module for you");
		this.registerCommand();
	}

	@Override
	public void onCommand(String[] args) 
	{
		for(Module module : Main.modules)
		{
			System.out.println(module.getName() + " " + args[1]);
			if(module.getName().equalsIgnoreCase(args[1]))
			{
				if(module.isEnabled())
				{
					module.disable();
				}
				else
				{
					module.enable();
				}
			}
		}
	}
}
