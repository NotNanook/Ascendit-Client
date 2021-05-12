package me.ascendit.util;

import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtils {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static int findItemInHot(Item item) {
		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[itemSlot];
			if (itemStack != null && itemStack.getItem() == item)
				return itemSlot;
		}
		return -1;
	}

	private static float getItemDamage(ItemStack itemStack) {
		Multimap multimap = itemStack.getAttributeModifiers();
		if (!multimap.isEmpty()) {
			Iterator<Map.Entry> iterator = multimap.entries().iterator();
			if (iterator.hasNext()) {
				double damage;
				Map.Entry entry = iterator.next();
				AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
				if (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {
					damage = attributeModifier.getAmount();
				} else {
					damage = attributeModifier.getAmount() * 100.0D;
				}
				if (attributeModifier.getAmount() > 1.0D)
					return 1.0F + (float) damage;
				return 1.0F;
			}
		}
		return 1.0F;
	}

	public static int getBestWeapon(Entity target) {
		int originalSlot = mc.thePlayer.inventory.currentItem;
		int weaponSlot = -1;
		float weaponDamage = 1.0F;
		byte slot;
		for (slot = 0; slot < 9; slot = (byte) (slot + 1)) {
			mc.thePlayer.inventory.currentItem = slot;
			ItemStack itemStack = mc.thePlayer.getHeldItem();
			if (itemStack != null) {
				float damage = getItemDamage(itemStack);
				damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
				if (damage > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}
		if (weaponSlot != -1)
			return weaponSlot;
		return originalSlot;
	}
}