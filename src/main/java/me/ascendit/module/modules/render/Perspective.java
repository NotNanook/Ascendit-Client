package me.ascendit.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.KeyEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;

// mixin-based module
@ModuleInfo(name = "Perspective", description = "Lets you move the camera freely around your character", category = Category.RENDER)
public class Perspective extends Module {
	
    public boolean perspectiveToggled = false;
    private float cameraYaw = 0F;
    private float cameraPitch = 0F;
    private int previousPerspective = 0;

    @EventTarget
    public void onKey(final KeyEvent event) {
        if (event.getKey() == Keyboard.KEY_X) {
            if (Keyboard.getEventKeyState()) {
                perspectiveToggled = !perspectiveToggled;
                cameraYaw = mc.thePlayer.rotationYaw;
                cameraPitch = mc.thePlayer.rotationPitch;

                if (perspectiveToggled) {
                    previousPerspective = mc.gameSettings.thirdPersonView;
                    mc.gameSettings.thirdPersonView = 1;
                } else {
                    mc.gameSettings.thirdPersonView = previousPerspective;
                }
            }
        } else if (event.getKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
            perspectiveToggled = false;
        }
    }

    public float getCameraYaw() {
        return perspectiveToggled ? cameraYaw : mc.thePlayer.rotationYaw;
    }

    public float getCameraPitch() {
        return perspectiveToggled ? cameraPitch : mc.thePlayer.rotationPitch;
    }

    public boolean overrideMouse() {
        if (mc.inGameHasFocus && Display.isActive()) {
            if (!perspectiveToggled) {
                return true;
            }

            mc.mouseHelper.mouseXYChange();
            final float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            final float f2 = f1 * f1 * f1 * 8.0F;
            final float f3 = (float) mc.mouseHelper.deltaX * f2;
            final float f4 = (float) mc.mouseHelper.deltaY * f2;

            cameraYaw += f3 * 0.15F;
            cameraPitch += f4 * 0.15F;

            if (cameraPitch > 90)
            	cameraPitch = 90;
            if (cameraPitch < -90)
            	cameraPitch = -90;
        }
        return false;
    }

}