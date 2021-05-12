package me.ascendit.module.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.AttackEvent;
import me.ascendit.event.events.MotionEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.module.modules.misc.AntiBot;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.util.timer.Timer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;

@ModuleInfo(name = "KillAura", description = "Attacks all targets around you", category = Category.COMBAT)
public class KillAura extends Module {
	
	private final BoolSetting noswing = new BoolSetting("NoSwing", false);
	private final ModeSetting targetmode = new ModeSetting("Targets", "entities", "players");
	private final DoubleSetting range = new DoubleSetting("Range", 4.0, 0.1, 10.0);
	private final IntSetting aps = new IntSetting("APS", 10, 1, 40);

	private final Timer timer = new Timer();

	public KillAura() {
		addSettings(targetmode, range, aps, noswing);
	}
	
	@EventTarget
	public void onMotion(final MotionEvent event) {
		addSettingtoName(targetmode);
		
		List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.get() && entity != mc.thePlayer && !entity.isDead && !(entity instanceof EntityArmorStand)).collect(Collectors.toList());
		targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
		
		if (targetmode.getMode().equalsIgnoreCase("players"))
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		
		if (targets.isEmpty() || AntiBot.isBot(targets.get(0)))
			return;
		
		final Entity target = targets.get(0);
		
		if (ModuleManager.teams.isToggled() && target instanceof EntityPlayer && ((EntityPlayer) target).getTeam() == mc.thePlayer.getTeam())
			return;
		
		event.setYaw(getRotations(target)[0]);
		event.setPitch(getRotations(target)[1]);
		
		if (timer.hasTimeElapsed(1000 / aps.get(), true)) {
			new AttackEvent(target).call();
			
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			
			if (!noswing.get())
				mc.thePlayer.swingItem();
		}
	}

	private float[] getRotations(final Entity entity) {
		final double deltaX = entity.posX + (entity.posX - entity.lastTickPosX) - mc.thePlayer.posX;
		final double deltaY = entity.posY - 3.5 + entity.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
		final double deltaZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) - mc.thePlayer.posZ; 
		final double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
		
		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		final float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		
		if (deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ) / deltaX));
		}
		
		return new float[] {yaw, pitch};
	}
}