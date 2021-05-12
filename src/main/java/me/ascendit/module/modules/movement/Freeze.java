package me.ascendit.module.modules.movement;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.PacketEvent;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "Freeze", description = "Freezes your movement", category = Category.MOVEMENT)
public class Freeze extends Module {
	
	@EventTarget
    public void onPacket(final PacketEvent event) {
    	final Packet<?> packet = event.getPacket();
    	if ((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction)) {
            event.setCancelled(true);
        }
    }
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
		mc.thePlayer.fallDistance = 0;
		
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
	}
}