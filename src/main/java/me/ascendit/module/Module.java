package me.ascendit.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ascendit.Ascendit;
import me.ascendit.event.EventManager;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.setting.Setting;
import me.ascendit.ui.notification.Notification;
import me.ascendit.ui.notification.NotificationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Timer;

public class Module {
	
	/*
	 * Module variables
	 */
	private final String name, description;
	private String displayname;
    private final Category category;
    private boolean toggled, silent;
    private int keybind;
    private final List<Setting> settings = new ArrayList<Setting>();
    
    /*
     * Minecraft variables
     */
    protected final Minecraft mc = Minecraft.getMinecraft();
    public static Timer timer = new Timer(20.0F);
    
    /*
     * TabGUI 
     */
    public int settingindex;
    public boolean moduleexpanded;

    /*
     * Module Constructor
     */
    public Module() {
    	ModuleInfo moduleinfo = getClass().getAnnotation(ModuleInfo.class);
        this.name = moduleinfo.name();
        this.displayname = moduleinfo.name();
        this.description = moduleinfo.description();
        this.category = moduleinfo.category();
        this.silent = moduleinfo.silent();
        this.toggled = false;
        this.keybind = 999;
        ModuleManager.modules.add(this);
    }
    
    /*
     * Toggle function
     */
    public void toggle() {
    	toggled = !toggled;
    	onToggle();
    	
    	if (toggled) {
    		onEnable();
    		EventManager.register(this);
    	} else {
    		onDisable();
    		EventManager.unregister(this);
    	}
    	
    	Ascendit.saveload.save();
    	if (!isSilent()) {
        	mc.thePlayer.playSound("random.click", 0.5F, 1F);
        	NotificationManager.show(new Notification(name + (toggled ? " enabled" : " disabled"), 1));
    	}
    }
    
    /*
     * Module methods
     */
    public void onEnable() {}
    public void onDisable() {}
    public void onToggle() {}

	public void addSettings(final Setting... settings) {
    	this.settings.addAll(Arrays.asList(settings));
    }
    
	public void addSettingtoName(final Setting setting) {
		if (setting instanceof BoolSetting)
			setDisplayName(getName() + " " + EnumChatFormatting.GRAY + String.valueOf(((BoolSetting)setting).get()).toUpperCase());
		else if (setting instanceof DoubleSetting)
			setDisplayName(getName() + " " + EnumChatFormatting.GRAY + String.valueOf(((DoubleSetting)setting).get()).toUpperCase());
		else if (setting instanceof FloatSetting)
			setDisplayName(getName() + " " + EnumChatFormatting.GRAY + String.valueOf(((FloatSetting)setting).get()).toUpperCase());
		else if (setting instanceof IntSetting)
			setDisplayName(getName() + " " + EnumChatFormatting.GRAY + String.valueOf(((IntSetting)setting).get()).toUpperCase());
		else if (setting instanceof ModeSetting)
			setDisplayName(getName() + " " + EnumChatFormatting.GRAY + ((ModeSetting)setting).getMode().toUpperCase());
	}
	
    public static Module getModulebyName(final String name) {
		for (final Module module : ModuleManager.modules) {
			if (module.getName().equalsIgnoreCase(name)) {
				return module;
			}
		}
		return null;
	}
    
    public static List<Module> getModulesbyCategory(final Category category) {
    	final List<Module> modules = new ArrayList<Module>();
    	
    	for (final Module module: ModuleManager.modules) {
    		if (module.getCategory() == category)
    			modules.add(module);
    	}
    	return modules;
    }

    /*
     * Getters and Setters
     */
 	public String getName() {
 		return name;
 	}

 	public String getDescription() {
 		return description;
 	}
 	
 	public Category getCategory() {
 		return category;
 	}
 	
 	public void setToggled(final boolean enabled) {
 		this.toggled = enabled;
 	}
 	
    public boolean isToggled() {
        return toggled;
    }

    public boolean isSilent() {
    	return silent;
    }
    
    public void setSilent(final boolean silent) {
    	this.silent = silent;
    }
    
	public List<Setting> getSettings() {
		return settings;
	}

	public String getDisplayName() {
		return displayname;
	}

	public void setDisplayName(final String displayname) {
		this.displayname = displayname;
	}

	public int getKeybind() {
		return keybind;
	}

	public void setKeybind(final int keybind) {
		this.keybind = keybind;
	}
}