package me.ascendit.event.events;

import me.ascendit.event.Event;

import net.minecraft.entity.Entity;

// Called in MixinPlayerControllerMP and KillAura
public class AttackEvent extends Event {

	private Entity targetentity;

	public AttackEvent(Entity targetentity) {
		this.targetentity = targetentity;
	}

	public Entity getTargetEntity() {
		return targetentity;
	}
}