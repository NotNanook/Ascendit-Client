package me.ascendit.mixin.mixins.gui;

import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.ascendit.Ascendit;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

@Mixin({GuiMainMenu.class})
public class MixinGuiMainMenu extends GuiScreen {
	@Inject(method = {"drawScreen"}, at = {@At("TAIL")}, cancellable = true)
	public void drawText(CallbackInfo callbackinfo) {
		Display.setTitle("");
		mc.fontRendererObj.drawString(Ascendit.MODNAME + " " + Ascendit.VERSION, width - fontRendererObj.getStringWidth(Ascendit.MODNAME + " " + Ascendit.VERSION) - 2, 2, -1, true);
		mc.fontRendererObj.drawString(Ascendit.moduleamount + " modules and", width - fontRendererObj.getStringWidth(Ascendit.moduleamount + " modules and") - 2, 12, -1, true);
		mc.fontRendererObj.drawString(Ascendit.commandamount + " unique commands", width - fontRendererObj.getStringWidth(Ascendit.commandamount + " unique commands") - 2, 22, -1, true);
	}
	
	@Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
	public void initGui(CallbackInfo callbackinfo) {
		Ascendit.discordrp.update("In The Main Menu", "");
	}
}
