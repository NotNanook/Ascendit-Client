package me.ascendit.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer {}