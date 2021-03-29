package me.ascendit.modules.combat;

import java.util.Random;

import org.lwjgl.input.Mouse;

import me.ascendit.modules.Category;
import me.ascendit.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;

public class ModuleAutoclicker extends Module {

    private Random random;   
    
    private int min = 10;
    private int max = 20;
    
    private long leftLastSwing = 0L;
    private long rightLastSwing = 0L;
        
    public ModuleAutoclicker() {
        super("Autoclicker", "Automatically clicks for you while you hold down Left Click", Category.COMBAT);
        this.registerModule();
        this.random = new Random();
        this.mode = "left";
    }
    
    @Override
    public void onTick() {
    	if (this.mode.equalsIgnoreCase("left") || this.mode.equalsIgnoreCase("both")) {
    		// check if mouse is pressed and if player is not blocking
    		if (Mouse.isButtonDown(0) && !mc.thePlayer.isBlocking()) {
    			// only click if the current system-time minus the last time clicked is bigger than the random number
    			if (System.currentTimeMillis() - leftLastSwing >= random.nextInt(max - min) + min) {
    				// check if player is looking at a block
    				if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
    					clickMouse(mc.gameSettings.keyBindAttack.getKeyCode());
    					// set the last time clicked to the current system-time
    					leftLastSwing = System.currentTimeMillis();
    				} else if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
    					KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
    					// set the last time clicked to the current system-time
    					leftLastSwing = System.currentTimeMillis();
    				}
    			}
    		}
    	}
    	if (this.mode.equalsIgnoreCase("right") || this.mode.equalsIgnoreCase("both")) {
    		// check if mouse is pressed, if player is not pausing and if player is not blocking and if he's not holding specific items
    		if (Mouse.isButtonDown(1) && !mc.thePlayer.isBlocking() && (mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem) != null && !(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() instanceof net.minecraft.item.ItemFood)) && !(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() instanceof net.minecraft.item.ItemBucketMilk) && !(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() instanceof net.minecraft.item.ItemPotion) && !(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() instanceof net.minecraft.item.ItemEnderPearl) && !(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() instanceof net.minecraft.item.ItemEnderEye)) {
    			// only click if the current system-time minus the last time clicked is bigger than the random number
    			if (System.currentTimeMillis() - rightLastSwing >= random.nextInt(max - min) + min) {
    				clickMouse(mc.gameSettings.keyBindUseItem.getKeyCode());
    				// set the last time clicked to the current system-time
    				rightLastSwing = System.currentTimeMillis();
    			}
    		}
    	}
    }
    
    private void clickMouse(int button) {
    	KeyBinding.setKeyBindState(button, true);
		KeyBinding.onTick(button);
		KeyBinding.setKeyBindState(button, false);
    }
    
    public void setMode(String mode)
    {
		this.mode = mode;
    }
}
