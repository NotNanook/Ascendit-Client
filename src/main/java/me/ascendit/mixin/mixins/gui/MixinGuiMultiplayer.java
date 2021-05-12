package me.ascendit.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.ascendit.Ascendit;

import net.minecraft.client.gui.GuiMultiplayer;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer {
	@Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
	public void initGui(CallbackInfo callbackinfo) {
		Ascendit.discordrp.update("In The Main Menu", "");
	}
}
