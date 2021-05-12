package me.ascendit;

import org.lwjgl.opengl.Display;

import me.ascendit.command.CommandManager;
import me.ascendit.config.SaveLoad;
import me.ascendit.discord.DiscordRP;
import me.ascendit.event.EventManager;
import me.ascendit.module.ModuleManager;

import net.minecraftforge.fml.common.Mod;

@Mod(name = Ascendit.MODNAME, modid = Ascendit.MODID, version = Ascendit.VERSION)
public class Ascendit {
	
	public static final SaveLoad saveload = new SaveLoad();
	public static final DiscordRP discordrp = new DiscordRP();

	public static final String MODNAME = "Ascendit";
    public static final String MODID = "ascendit";
    public static final String VERSION = "v1.0";
    
    public static final int moduleamount = ModuleManager.modules.size();
    public static final int commandamount = CommandManager.commands.size();
    
    public static void init() {
    	System.out.println("Successfully launched " + Ascendit.MODNAME + " " + Ascendit.VERSION);
    	discordrp.start();
    	Display.setTitle("");
    	saveload.load();
        EventManager.registerEventClasses();
    }
    
    public static void terminate() {
    	System.out.println("Stopping " + Ascendit.MODNAME + " " + Ascendit.VERSION + "...");
    	discordrp.shutdown();
    	saveload.save();
    }
}