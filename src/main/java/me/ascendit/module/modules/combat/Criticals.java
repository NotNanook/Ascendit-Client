package me.ascendit.module.modules.combat;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.AttackEvent;
import me.ascendit.event.events.TickEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.ModeSetting;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Criticals", description = "Always deal critical hits", category = Category.COMBAT)
public class Criticals extends Module {

	private ModeSetting mode = new ModeSetting("Mode", "packet", "ncppacket", "hop", "tphop", "jump", "lowhop", "visual");

	public Criticals() {
		addSettings(mode);
	}

	@EventTarget
	public void onTick(final TickEvent event) {
		addSettingtoName(mode);
	}

	@EventTarget
	public void onAttack(final AttackEvent event) {
		EntityPlayerSP thePlayer = mc.thePlayer;

		if (!(event.getTargetEntity() instanceof EntityLivingBase))
			return;

		EntityLivingBase entity = ((EntityLivingBase) event.getTargetEntity());
		
		if (!thePlayer.onGround || thePlayer.isOnLadder())
			return;

		double x = thePlayer.posX;
		double y = thePlayer.posY;
		double z = thePlayer.posZ;

		switch (mode.getMode().toLowerCase()) {
			case "packet":
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, true));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
				thePlayer.onCriticalHit(entity);
			case "ncppacket":
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1100013579, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0000013579, z, false));
				thePlayer.onCriticalHit(entity);
			case "hop":
				thePlayer.motionY = 0.1;
				thePlayer.fallDistance = 0.1f;
				thePlayer.onGround = false;
			case "tphop":
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.02, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
				thePlayer.setPosition(x, y + 0.01, z);
			case "jump":
				thePlayer.jump();
			case "lowhop":
				thePlayer.motionY = 0.3425;
			case "visual":
				thePlayer.onCriticalHit(entity);
		}
	}
}