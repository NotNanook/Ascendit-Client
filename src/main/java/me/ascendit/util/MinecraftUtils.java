package me.ascendit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class MinecraftUtils {

	private final static Minecraft mc = Minecraft.getMinecraft();

	public static Field getField(final Class c, final String fieldname) throws NoSuchFieldException {
		try {
			final Field field = c.getDeclaredField(fieldname);
			field.setAccessible(true);
			return field;
		} catch (final NoSuchFieldException e) {
			final Class superclass = c.getSuperclass();
			if (superclass == null)
				throw e;
			return getField(superclass, fieldname);
		}
	}

	public static boolean isHoldingblock() {
		return ((mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem) != null
				&& (mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem)
						.getItem() instanceof ItemBlock)));
	}

	public static void sendMessage(final String msg) {
		final ChatComponentText message = new ChatComponentText(
				"\2478[\2474\247lAscendit\2478] " + EnumChatFormatting.GRAY + msg);
		message.getChatStyle().setColor(EnumChatFormatting.GRAY);
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(message);
	}

	public static void sendMessagewithoutPrefix(final String msg) {
		final ChatComponentText message = new ChatComponentText(EnumChatFormatting.GRAY + msg);
		message.getChatStyle().setColor(EnumChatFormatting.GRAY);
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(message);
	}

	public static void clickMouse() {
		try {
			Method method;
			try {
				method = mc.getClass().getDeclaredMethod("func_147116_af", new Class[0]);
			} catch (final NoSuchMethodException e) {
				method = mc.getClass().getDeclaredMethod("clickMouse", new Class[0]);
			}
			method.setAccessible(true);
			method.invoke(mc, new Object[0]);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void rightClickMouse() {
		try {
			Method method;
			try {
				method = mc.getClass().getDeclaredMethod("func_147121_ag", new Class[0]);
			} catch (final NoSuchMethodException e) {
				method = mc.getClass().getDeclaredMethod("rightClickMouse", new Class[0]);
			}
			method.setAccessible(true);
			method.invoke(mc, new Object[0]);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}
}