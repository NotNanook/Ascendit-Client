package me.ascendit;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import me.ascendit.events.onInteractEvent;
import me.ascendit.events.onKeyInputEvent;
import me.ascendit.events.onLivingUpdateEvent;
import me.ascendit.events.onPacketOutEvent;
import me.ascendit.modules.Module;
import me.ascendit.modules.combat.ModuleProjectileAimer;
import me.ascendit.modules.misc.ModuleEasyHiveBed;
import me.ascendit.modules.misc.ModuleFastplace;
import me.ascendit.modules.render.ModuleESP;
import me.ascendit.network.ChannelHandlerInput;
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
    public static ModuleESP esp;
    public static ModuleEasyHiveBed easyHiveBed;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Display.setTitle("Ascendit Client");
    	
    	// modules
    	modules = new ArrayList<Module>();
    	fastplace = new ModuleFastplace();
    	projectileAimer = new ModuleProjectileAimer();
    	esp = new ModuleESP();
    	easyHiveBed = new ModuleEasyHiveBed();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {	
    	// events
    	MinecraftForge.EVENT_BUS.register(new onKeyInputEvent());
    	MinecraftForge.EVENT_BUS.register(new onLivingUpdateEvent());
    	MinecraftForge.EVENT_BUS.register(new onInteractEvent());
    	
    	
    	// network
    	MinecraftForge.EVENT_BUS.register(new ChannelHandlerInput());
    	MinecraftForge.EVENT_BUS.register(new onPacketOutEvent());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
