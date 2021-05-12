package me.ascendit.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.ascendit.event.events.AttackEvent;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
	@Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V"))
	public void attackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo callbackInfo) {
		new AttackEvent(targetEntity).call();
	}
}