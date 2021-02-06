package me.ascendit.modules;

import me.ascendit.Main;

public abstract class Module {
	
	private String name;
	private String description;
	private Category category;
	private boolean enabled;
	
	public Module(String name, String description, Category category)
	{
		this.name = name;
		this.description = description;
		this.category = category;
		this.enabled = false;
	}
	
	public void registerModule()
	{
		Main.modules.add(this);
	}
	
	public abstract void onTick();
}
