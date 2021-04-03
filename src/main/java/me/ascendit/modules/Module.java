package me.ascendit.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.ascendit.Main;
import me.ascendit.events.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class Module implements Comparable<Module>{
	
	protected String name;
	protected String description;
	protected Category category;
	protected boolean enabled;
	protected int keyBind;
	protected Minecraft mc;
	protected String mode;
	protected ArrayList<String> modes;
	protected boolean visible;
	
	public Module(String name, String description, Category category)
	{
		this.name = name;
		this.description = description;
		this.category = category;
		this.enabled = false;
		this.keyBind = Keyboard.CHAR_NONE;
		this.mc = Minecraft.getMinecraft();
		this.visible = true;
	}
	
	public void registerModule()
	{
		Main.modules.addModule(this);
	}
	
	public void enable()
	{
		enabled = true;
		this.sendMessage(this.getName() + " is now enabled", EnumChatFormatting.GREEN);
		this.onEnable();
	}
	
	public void disable()
	{
		enabled = false;
		this.sendMessage(this.getName() + " is now disabled", EnumChatFormatting.RED);
		this.onDisable();
	}
	
	public void onEnable() {}
	
	public void onDisable() {}
	
	public void onTick() {}
	
	public void onInteract(PlayerInteractEvent event) {}
	
	public void onRender2d(RenderGameOverlayEvent.Text event) {}
	
	public void onRender3d(RenderWorldLastEvent event) {}
	
	public void onPacketOut(PacketEvent.Outgoing event) {}
	
	public void sendMessage(String msg, EnumChatFormatting color)
	{
		ChatComponentText message = new ChatComponentText(msg);
		message.getChatStyle().setColor(color);
		mc.thePlayer.addChatComponentMessage(message);
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public int getKeyBind()
	{
		return keyBind;
	}
	
	public void setKeyBind(int keyBind)
	{
		this.keyBind = keyBind;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setMode(String mode)
	{
		this.mode = mode;
	}
	
	public String getMode()
	{
		return this.mode;
	}
	
	public ArrayList<String> getModes()
	{
		return this.modes;
	}
	
	public boolean isVisible()
	{
		return this.visible;
	}
	
	// for sorting by module length
	@Override
	public int compareTo(Module compareto)
	{
		int thisLength = mc.fontRendererObj.getStringWidth(this.name);
		int thatLength = mc.fontRendererObj.getStringWidth(compareto.name);

		return thatLength - thisLength;
	}
}
