package me.ascendit.utils;

import java.util.ArrayList;
import java.util.Collections;

import me.ascendit.modules.Module;

public class ModuleList {
	
	public ArrayList<Module> moduleList;
	
	public ModuleList(ArrayList<Module> moduleList)
	{
		this.moduleList = moduleList;
	}
	
	public void addModule(Module module)
	{
		this.moduleList.add(module);
	}
	
	public void sortByLength()
	{
		Collections.sort(this.moduleList);
	}
}
