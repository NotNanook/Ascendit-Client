package me.ascendit.mixin.mixins.client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.ascendit.Ascendit;
import me.ascendit.event.events.KeyEvent;
import me.ascendit.event.events.TickEvent;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleManager;
import me.ascendit.util.CPSCounter;
import me.ascendit.util.Icon;
import me.ascendit.util.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Timer;
import net.minecraft.world.WorldSettings;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Shadow
	public GuiScreen currentScreen;

	@Shadow
	private Timer timer = new Timer(20.0F);

	@Shadow
	private int leftClickCounter;

	@Shadow
	private int rightClickDelayTimer;

	@Inject(method = "clickMouse", at = @At(value = "HEAD"))
	private void clickMouse(final CallbackInfo callbackinfo) {
		CPSCounter.addLeftClick();

		if (ModuleManager.autoclicker.isToggled())
			leftClickCounter = 0;
	}

	@Inject(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER))
	private void createDisplay(final CallbackInfo callbackInfo) {
		Display.setTitle("");
	}

	@Inject(method = "launchIntegratedServer", at = @At(value = "TAIL"))
	private void launchIntegratedServer(final String folderName, final String worldName,
			final WorldSettings worldSettingsIn, final CallbackInfo callbackinfo) {
		Ascendit.discordrp.update("Playing Singleplayer", "");
	}

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
	private void onKey(final CallbackInfo callbackInfo) {
		if (Keyboard.getEventKeyState() && currentScreen == null)
			new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey())
					.call();
	}

	@Inject(method = "rightClickMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelayTimer:I", shift = At.Shift.AFTER))
	private void rightClickMouse(final CallbackInfo callbackinfo) {
		CPSCounter.addRightClick();

		if (ModuleManager.fastplace.isToggled()
				&& (ModuleManager.fastplace.onlyblocks.get() ? MinecraftUtils.isHoldingblock() : (true)))
			rightClickDelayTimer = ModuleManager.fastplace.delay.get();
	}

	@Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift = At.Shift.BEFORE))
	private void runTick(final CallbackInfo callbackInfo) {
		timer = Module.timer;
		new TickEvent().call();
	}

	@Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
	private void setWindowIcon(final CallbackInfo callbackInfo) {
		if (Icon.getIcon() != null)
			Display.setIcon(Icon.getIcon());
		callbackInfo.cancel();
	}

	@Inject(method = "shutdown", at = @At("HEAD"))
	private void shutdown(final CallbackInfo callbackInfo) {
		Ascendit.terminate();
	}

	@Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
	private void startGame(final CallbackInfo callbackInfo) {
		Ascendit.init();
	}
}