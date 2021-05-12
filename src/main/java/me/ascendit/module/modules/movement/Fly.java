package me.ascendit.module.modules.movement;

import org.lwjgl.input.Keyboard;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.UpdateEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.ModeSetting;

@ModuleInfo(name = "Fly", description = "Allows you to fly", category = Category.MOVEMENT)
public class Fly extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", "vanilla", "smoothvanilla", "aac3.3.12", "hypixel", "redesky");
	public final FloatSetting vanillaspeed = new FloatSetting("VanillaSpeed", 0.1F, 0.1F, 10.0F);
	public final FloatSetting timerspeed = new FloatSetting("TimerSpeed", 0.1F, 0.1F, 20F);
	public final DoubleSetting aacmotion = new DoubleSetting("AACMotion", 7.0, 0.1, 10.0);
	
    public Fly() {
        addSettings(mode, vanillaspeed, timerspeed, aacmotion);
    }

    @Override
    public void onDisable() {
    	mc.thePlayer.capabilities.isFlying = false;
    	mc.thePlayer.stepHeight = 0.6F;
    	timer.timerSpeed = 1.0F;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	addSettingtoName(mode);
    	switch (mode.getMode().toLowerCase()) {
	    	case "vanilla":
	    		mc.thePlayer.capabilities.isFlying = true;
	
	    		if (mc.gameSettings.keyBindJump.isPressed())
	    			mc.thePlayer.motionY += 0.1;
	
	    		if (mc.gameSettings.keyBindSneak.isPressed())
	    			mc.thePlayer.motionY -= 0.1;
	
	    		if (mc.gameSettings.keyBindForward.isPressed())
	    			mc.thePlayer.capabilities.setFlySpeed(vanillaspeed.get());
	
	    		break;
	    	case "smoothvanilla":
	    		mc.thePlayer.capabilities.isFlying = true;
	    		break;
	    	case "aac3.3.12":
	    		if (mc.thePlayer.posY < -70) {
	    			mc.thePlayer.motionY = aacmotion.get();
	    		}
	    		timer.timerSpeed = 1.0F;
	    		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
	    			timer.timerSpeed = 0.2F;
	    		}
	    		break;
	    	case "hypixel":
	    		int count = 0;
	    		if (mc.gameSettings.keyBindForward.isKeyDown()) {
	    			mc.thePlayer.stepHeight = 0.0F;
	    			count++;
	    			if (count == 2) {
	    				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10D, mc.thePlayer.posZ);
	    				count = 0;
	    			}
	    			mc.thePlayer.motionY = 0.0D;
	    			mc.thePlayer.onGround = true;
	    		}
	    		if (mc.gameSettings.keyBindJump.isPressed()) {
	    			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4D, mc.thePlayer.posZ);
	    		}
	    		break;
	    	case "redesky":
	    		timer.timerSpeed = 1.0F;
	    		mc.thePlayer.capabilities.isFlying = true;
	
	    		if (mc.gameSettings.keyBindJump.isPressed()) {
	    			mc.thePlayer.motionY += 0.1;
	    			timer.timerSpeed = 2.0F;
	    		}
	
	    		if (mc.gameSettings.keyBindSneak.isPressed()) {
	    			mc.thePlayer.motionY -= 0.1;
	    			timer.timerSpeed = 2.0F;    			
	    		}
	
	    		if (mc.gameSettings.keyBindForward.isPressed()) {
	    			mc.thePlayer.capabilities.setFlySpeed(vanillaspeed.get());
	    			timer.timerSpeed = 2.0F;
	    		}
	    		break;
	    	}
    }
}