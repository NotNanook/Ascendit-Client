package me.ascendit;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import me.ascendit.commands.Command;
import me.ascendit.commands.CommandBind;
import me.ascendit.commands.CommandFakeCheater;
import me.ascendit.commands.CommandFastplace;
import me.ascendit.commands.CommandFurnaceCommand;
import me.ascendit.commands.CommandToggle;
import me.ascendit.events.onInteractEvent;
import me.ascendit.events.onKeyInputEvent;
import me.ascendit.events.onLivingUpdateEvent;
import me.ascendit.events.onPacketOutEvent;
import me.ascendit.events.onRender2d;
import me.ascendit.events.onRender3d;
import me.ascendit.modules.Module;
import me.ascendit.modules.combat.ModuleAutoclicker;
import me.ascendit.modules.combat.ModuleProjectileAimer;
import me.ascendit.modules.misc.ModuleEasyHiveBed;
import me.ascendit.modules.misc.ModuleFakeCheater;
import me.ascendit.modules.misc.ModuleFastplace;
import me.ascendit.modules.movement.ModuleSprint;
import me.ascendit.modules.render.ModuleCameraClip;
import me.ascendit.modules.render.ModuleESP;
import me.ascendit.modules.render.ModuleFullbright;
import me.ascendit.modules.render.ModuleModuleList;
import me.ascendit.modules.render.ModuleTrueSight;
import me.ascendit.network.ChannelHandlerInput;
import me.ascendit.utils.ModuleList;
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
    public static ModuleList modules;
    public static ModuleFastplace fastplace;
    public static ModuleProjectileAimer projectileAimer;
    public static ModuleESP esp;
    public static ModuleEasyHiveBed easyHiveBed;
    public static ModuleSprint sprint;
    public static ModuleFullbright fullbright;
    public static ModuleModuleList moduleList;
    public static ModuleAutoclicker autoclicker;
    public static ModuleFakeCheater fakeCheater;
    public static ModuleCameraClip cameraClip;
    public static ModuleTrueSight trueSight;
    
    // commands
    public static ArrayList<Command> commands;
    public static Command toggle;
    public static Command setDelay;
    public static Command furnace;
    public static Command bind;
    public static Command fakeCheat;
    public static Command help;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Display.setTitle("Ascendit Client");
    	
    	// modules
    	modules = new ModuleList(new ArrayList<Module>());
    	fastplace = new ModuleFastplace();
    	projectileAimer = new ModuleProjectileAimer();
    	esp = new ModuleESP();
    	easyHiveBed = new ModuleEasyHiveBed();
    	sprint = new ModuleSprint();
    	fullbright = new ModuleFullbright();
    	moduleList = new ModuleModuleList();
    	autoclicker = new ModuleAutoclicker();
    	fakeCheater = new ModuleFakeCheater();
    	cameraClip = new ModuleCameraClip();
    	trueSight = new ModuleTrueSight();
    	
    	// commands
    	commands = new ArrayList<Command>();
    	toggle = new CommandToggle();
    	setDelay = new CommandFastplace();
    	furnace = new CommandFurnaceCommand();
    	bind = new CommandBind();
    	fakeCheat = new CommandFakeCheater();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {	
    	// events
    	MinecraftForge.EVENT_BUS.register(new onKeyInputEvent());
    	MinecraftForge.EVENT_BUS.register(new onLivingUpdateEvent());
    	MinecraftForge.EVENT_BUS.register(new onInteractEvent());
    	MinecraftForge.EVENT_BUS.register(new onRender2d());
    	MinecraftForge.EVENT_BUS.register(new onRender3d());
    	
    	// network
    	MinecraftForge.EVENT_BUS.register(new ChannelHandlerInput());
    	MinecraftForge.EVENT_BUS.register(new onPacketOutEvent());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
