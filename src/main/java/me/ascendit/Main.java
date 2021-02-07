package me.ascendit;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import me.ascendit.events.onTickEvent;
import me.ascendit.modules.Module;
import me.ascendit.modules.combat.ModuleProjectileAimer;
import me.ascendit.modules.misc.ModuleFastplace;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main
{
    public static final String MODID = "ascendit";
    public static final String VERSION = "0.1";
    
    // modules
    public static ArrayList<Module> modules;
    public static ModuleFastplace fastplace;
    public static ModuleProjectileAimer projectileAimer;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Display.setTitle("Ascendit Client");
    	
    	// modules
    	modules = new ArrayList<Module>();
    	fastplace = new ModuleFastplace();
    	projectileAimer = new ModuleProjectileAimer();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new onTickEvent());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
