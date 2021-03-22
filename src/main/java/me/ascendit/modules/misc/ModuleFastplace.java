package me.ascendit.modules.misc;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModuleFastplace extends Module
{
	private int delay;
	private Field field;
	
	public ModuleFastplace() 
	{
		super("Fastplace", "Allows you to set the place delay to whatever you want", Category.MISC);
		this.registerModule();
		this.keyBind = Keyboard.KEY_P;
		this.delay = 1;
	}

	public void onEnable() 
	{
		field = ReflectionHelper.findField(Minecraft.class, "rightClickDelayTimer", "field_71467_ac", "ap");
	}

	public void onDisable() 
	{
	}

	public void onInteract(PlayerInteractEvent event) 
	{
		if(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem) != null && (mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() instanceof ItemBlock))
		{
			try {
				field.set(mc, delay);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setDelay(int delay)
	{
		this.delay = delay;
	}
}
