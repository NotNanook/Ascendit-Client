package me.ascendit.module.modules.combat;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.AttackEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

@ModuleInfo(name = "SuperKnockback", description = "Deals more knockback to entities", category = Category.COMBAT)
public class SuperKnockback extends Module {

	@EventTarget
	public void onAttack(final AttackEvent event) {
		if (!(event.getTargetEntity() instanceof EntityLivingBase))
			return;

		if (mc.thePlayer.isSprinting())
			mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));

		mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
		mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
		mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
		mc.thePlayer.setSprinting(true);

		try {
			MinecraftUtils.getField(EntityPlayerSP.class, "serverSprintState").setBoolean(mc.thePlayer, true);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}