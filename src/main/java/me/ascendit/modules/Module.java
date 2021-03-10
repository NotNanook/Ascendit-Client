package me.ascendit.modules;

import org.lwjgl.input.Keyboard;

import me.ascendit.Main;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Module {
	
	protected String name;
	protected String description;
	protected Category category;
	protected boolean enabled;
	protected int keyBind;
	protected Minecraft mc;
	
	public Module(String name, String description, Category category)
	{
		this.name = name;
		this.description = description;
		this.category = category;
		this.enabled = false;
		this.keyBind = Keyboard.CHAR_NONE;
		this.mc = Minecraft.getMinecraft();
	}
	
	public void registerModule()
	{		
		Main.modules.add(this);
	}
	
	public void enable()
	{
		enabled = true;
		this.onEnable();
		//mc.thePlayer.playSound("random.click", 0.5f, 1f);
	}
	
	public void disable()
	{
		enabled = false;
		this.onDisable();
		//mc.thePlayer.playSound("random.click", 0.5f, 1f);
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
	
	public abstract void onEnable();
	public abstract void onDisable();
	public abstract void onTick();
	public abstract void onInteract(PlayerInteractEvent event);
	public abstract void onRender2d(RenderGameOverlayEvent.Text event);
	public abstract void onRender3d(RenderWorldLastEvent event);
}
