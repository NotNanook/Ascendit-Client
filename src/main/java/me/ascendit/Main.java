package me.ascendit;

import java.util.ArrayList;

import me.ascendit.modules.Module;
import me.ascendit.modules.ModuleFastplace;
import me.ascendit.modules.ModuleProjectileAimer;
import net.minecraft.init.Blocks;
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
    
    public static ArrayList<Module> modules;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	modules = new ArrayList<Module>();
    	
    	// modules
    	ModuleFastplace fastplace = new ModuleFastplace();
    	ModuleProjectileAimer projectileAimer = new ModuleProjectileAimer();
    }
    
    public void init(FMLInitializationEvent event)
    {
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
