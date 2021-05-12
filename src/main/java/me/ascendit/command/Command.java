package me.ascendit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ascendit.event.EventManager;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.client.Minecraft;

public abstract class Command {

	/*
	 * Command Variables
	 */
    private final String command, syntax;
    private boolean toggled;
    private List<String> aliases = new ArrayList<String>();
    
    /*
     * Minecraft Variable
     */
    protected final Minecraft mc = Minecraft.getMinecraft();
    
    /*
     * Command Constructor
     */
	public Command(final String command, final String syntax, final String... aliases) {
        this.command = command;
        this.syntax = syntax;
        this.aliases = Arrays.asList(aliases);
        CommandManager.commands.add(this);
    }
	
	public void toggle() {
		toggled = !toggled;
		
		if (toggled)
			EventManager.register(this);
		else
			EventManager.unregister(this);
	}
	
	/*
	 * Command method
	 */
	public abstract void onCommand(String[] args);
	
	public void printSyntax() {
		MinecraftUtils.sendMessage("Syntax: \2478" + syntax);
		mc.thePlayer.playSound("random.anvil_land", 0.5F, 1F);
	}
	
	/*
	 * Getters
	 */
    public String getCommand() {
        return command;
    }

    public String getSyntax() {
        return syntax;
    }
    
    public List<String> getAliases() {
    	return this.aliases;
    }
}