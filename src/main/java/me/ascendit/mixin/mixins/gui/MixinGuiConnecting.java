package me.ascendit.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.ascendit.Ascendit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {
	@Inject(method = "connect", at = @At("TAIL"), cancellable = true)
	private void connect(final String ip, final int port, CallbackInfo callbackinfo) {
		Ascendit.discordrp.update("Playing on " + Minecraft.getMinecraft().getCurrentServerData().serverIP, "");
	}
}