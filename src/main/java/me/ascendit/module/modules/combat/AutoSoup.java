package me.ascendit.module.modules.combat;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.util.ItemUtils;
import me.ascendit.util.timer.MSTimer;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "AutoSoup", description = "Automatically uses soups to keep you alive", category = Category.COMBAT)
public class AutoSoup extends Module {

	private ModeSetting mode = new ModeSetting("Item", "soup", "potion");
	private IntSetting health = new IntSetting("Health", 15, 1, 20);
	private BoolSetting drop = new BoolSetting("Drop", true);
	private IntSetting delay = new IntSetting("Delay", 100, 0, 100);
	private IntSetting switchbackdelay = new IntSetting("SwitchBack Delay", 100, 0, 100);
	
	private MSTimer timer = new MSTimer();
	private MSTimer timer2 = new MSTimer();
	private Item item = null;
	private boolean back = false;

	public AutoSoup() {
		addSettings(mode, health, drop, delay, switchbackdelay);
	}
	
	@EventTarget
    public void onUpdate(final UpdateEvent event) {
		addSettingtoName(mode);
		if (!this.back && mc.thePlayer.getHealth() <= health.get() && timer.hasTimeElapsed(delay.get())) {
			eat();
			timer.reset();
			timer2.reset();
		}
		if (this.back && this.timer2.hasTimeElapsed(switchbackdelay.get())) {
			back();
			timer.reset();
			timer2.reset();
		}
	}

	private void eat() {
		if (mode.getMode().equalsIgnoreCase("soup"))
			this.item = Items.mushroom_stew;
		if (mode.getMode().equalsIgnoreCase("potion"))
			this.item = (Item) Items.potionitem;
		
		int soup = ItemUtils.findItemInHot(this.item);
		
		if (soup == -1)
			return;
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(soup));
		mc.thePlayer.inventory.currentItem = soup;
		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer.getSlot(soup).getStack()));
		if (drop.get())
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		this.back = true;
	}

	private void back() {
		int slot = ItemUtils.getBestWeapon((Entity) mc.thePlayer);
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
		mc.thePlayer.inventory.currentItem = slot;
		this.back = false;
	}
}