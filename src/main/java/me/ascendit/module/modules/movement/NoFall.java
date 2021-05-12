package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.ModeSetting;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage", category = Category.MOVEMENT)
public class NoFall extends Module {
	
	public final ModeSetting mode = new ModeSetting("mode", "packet", "aac3.3.15");
	
	public NoFall() {
		addSettings(mode);
	}
	
	@EventTarget
    public void onUpdate(final UpdateEvent event) {
		addSettingtoName(mode);
		switch (mode.getMode().toLowerCase()) {
			case "packet":
				if (mc.thePlayer.fallDistance >= 2F) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
			case "aac3.3.15":
				if (mc.thePlayer.fallDistance >= 2F) {
					mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, false));
					mc.thePlayer.fallDistance = (float) -9999;
				}
		}
	}
}