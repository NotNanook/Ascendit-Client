package me.ascendit.module.modules.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.util.timer.MSTimer;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

import scala.actors.threadpool.Arrays;

@ModuleInfo(name = "InventoryHelper", description = "AutoArmor and InventoryCleaner", category = Category.PLAYER)
public class InventoryHelper extends Module {

	private BoolSetting instant = new BoolSetting("Instant", false);
	private IntSetting mindelay = new IntSetting("MinDelay", 750, 0, 2000);
	private IntSetting maxdelay = new IntSetting("MaxDelay", 750, 0, 2000);
	private BoolSetting randomorder = new BoolSetting("RandomOrder", true);
	private BoolSetting preferswords = new BoolSetting("PreferSwords", true);

	private Random rd = new Random();
	private MSTimer timer = new MSTimer();
	private int[] bestArmorDamageReducement;
	private int[] bestArmorSlots;
	private float bestSwordDamage;
	private int bestSwordSlot;
	private List<Integer> trash = new ArrayList<>();

	public InventoryHelper() {
		addSettings(instant, mindelay, maxdelay, randomorder, preferswords);
	}

	@EventTarget
    public void onUpdate(UpdateEvent event) {
		searchforitems();

		for (int i = 0; i < 4; i++) {
			if (bestArmorSlots[i] != -1) {
				int bestslot = bestArmorSlots[i];
				ItemStack oldarmor = mc.thePlayer.inventory.armorItemInSlot(i);

				if (oldarmor != null && oldarmor.getItem() != null) {
					mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 1, mc.thePlayer);

					if (!isDelayReached()) {
						return;
					}
					timer.reset();
				}

				mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestslot < 9 ? bestslot + 36 : bestslot, 0, 1, mc.thePlayer);

				if (!isDelayReached()) {
					return;
				}
				timer.reset();
			}
		}

		if (bestSwordSlot != -1 && bestSwordDamage != -1) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot, 0, 2, mc.thePlayer);

			if (!isDelayReached()) {
				return;
			}
			timer.reset();
		}

		searchfortrash();

		if (randomorder.get())
			Collections.shuffle(trash);

		for (Integer integer : trash) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, integer < 9 ? integer + 36 : integer, 0, 4, mc.thePlayer); // mode 4 == Drop all

			if (!isDelayReached()) {
				return;
			}
			timer.reset();
		}
	}

	private void searchforitems() {
		bestArmorDamageReducement = new int[4];
		bestArmorSlots = new int[4];
		bestSwordDamage = -1;
		bestSwordSlot = -1;

		Arrays.fill(bestArmorDamageReducement, -1);
		Arrays.fill(bestArmorSlots, -1);

		for (int i = 0; i < bestArmorSlots.length; i++) {
			ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(i);

			if (itemstack != null && itemstack.getItem() != null) {
				if (itemstack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) itemstack.getItem();

					bestArmorDamageReducement[i] = armor.damageReduceAmount;
				}
			}
		}

		// 4 rows, 9 columns

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

			if (itemstack == null || itemstack.getItem() == null)
				continue;

			if (itemstack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemstack.getItem();

				int armortype = 3 - armor.armorType;

				if (bestArmorDamageReducement[armortype] < armor.damageReduceAmount) {
					bestArmorDamageReducement[armortype] = armor.damageReduceAmount;
					bestArmorSlots[armortype] = i;
				}
			}

			if (itemstack.getItem() instanceof ItemSword) {
				ItemSword sword = (ItemSword) itemstack.getItem();

				if (bestSwordDamage < sword.getDamageVsEntity()) {
					bestSwordDamage = sword.getDamageVsEntity();
					bestSwordSlot = i;
				}
			}

			if (itemstack.getItem() instanceof ItemTool) {
				ItemTool tool = (ItemTool) itemstack.getItem();

				float damage = tool.getToolMaterial().getDamageVsEntity();

				if (preferswords.get())
					damage -= 1.0F;

				if (bestSwordDamage < damage) {
					bestSwordDamage = damage;
					bestSwordSlot = i;
				}
			}
		}
	}

	private void searchfortrash() {
		trash.clear();

		bestArmorDamageReducement = new int[4];
		bestArmorSlots = new int[4];
		bestSwordDamage = -1;
		bestSwordSlot = -1;

		Arrays.fill(bestArmorDamageReducement, -1);
		Arrays.fill(bestArmorSlots, -1);

		List<Integer>[] allitems = new List[4];
		List<Integer> allswords = new ArrayList<>();

		for (int i = 0; i < bestArmorSlots.length; i++) {
			ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(i);

			allitems[i] = new ArrayList<>();

			if (itemstack != null && itemstack.getItem() != null) {
				if (itemstack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) itemstack.getItem();

					bestArmorDamageReducement[i] = armor.damageReduceAmount;
					bestArmorSlots[i] = 8 + i;
				}
			}
		}

		// 4 rows, 9 columns

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

			if (itemstack == null || itemstack.getItem() == null)
				continue;

			if (itemstack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemstack.getItem();

				int armortype = 3 - armor.armorType;

				allitems[armortype].add(i);

				if (bestArmorDamageReducement[armortype] < armor.damageReduceAmount) {
					bestArmorDamageReducement[armortype] = armor.damageReduceAmount;
					bestArmorSlots[armortype] = i;
				}
			}

			if (itemstack.getItem() instanceof ItemSword) {
				ItemSword sword = (ItemSword) itemstack.getItem();

				allswords.add(i);

				if (bestSwordDamage < sword.getDamageVsEntity()) {
					bestSwordDamage = sword.getDamageVsEntity();
					bestSwordSlot = i;
				}
			}

			if (itemstack.getItem() instanceof ItemTool) {
				ItemTool tool = (ItemTool) itemstack.getItem();

				float damage = tool.getToolMaterial().getDamageVsEntity();

				if (preferswords.get())
					damage -= 1.0F;

				if (bestSwordDamage < damage) {
					bestSwordDamage = damage;
					bestSwordSlot = i;
				}
			}
		}
		for (int i = 0; i < allitems.length; i++) {
			List<Integer> allitem = allitems[i];
			int finali = i;
			allitem.stream().filter(slot -> slot != bestArmorSlots[finali]).forEach(trash::add);
		}

		allswords.stream().filter(slot -> slot != bestSwordSlot).forEach(trash::add);
	}

	private boolean isDelayReached() {
		return timer.hasTimeElapsed(rd.nextInt(maxdelay.get() - mindelay.get()) + mindelay.get()) || instant.get();
	}
}