package me.ascendit.module.modules.render;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.MotionEvent;
import me.ascendit.event.events.PacketEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Rotations", description = "Shows serverside rotations", category = Category.RENDER)
public class Rotations extends Module {

	private Float playerYaw;

	@EventTarget
    public void onPacket(final PacketEvent event) {
		Packet<?> packet = event.getPacket();

		if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
			C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

			playerYaw = packetPlayer.getYaw();

			mc.thePlayer.renderYawOffset = packetPlayer.getYaw();
			mc.thePlayer.rotationYawHead = packetPlayer.getYaw();
		} else {
			if (playerYaw != null) {
				mc.thePlayer.renderYawOffset = this.playerYaw;
				mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset;
			}
		}
	}
	
	@EventTarget
	public void onMotion(final MotionEvent event) {
		mc.thePlayer.renderYawOffset = event.getYaw();
		mc.thePlayer.rotationYawHead = event.getYaw();
	}
}