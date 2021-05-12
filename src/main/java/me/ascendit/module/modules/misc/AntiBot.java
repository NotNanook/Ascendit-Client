package me.ascendit.module.modules.misc;

import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.BoolSetting;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "AntiBot", description = "Prevents modules from targeting bots", category = Category.MISC)
public class AntiBot extends Module {
	
	private static BoolSetting invisibles = new BoolSetting("Invisibles", true);
	private static BoolSetting onlyplayers = new BoolSetting("OnlyPlayers", true);

	public AntiBot() {
		addSettings(invisibles, onlyplayers);
	}

	public static boolean isBot(Entity entity) {
		if (!ModuleManager.antibot.isToggled())
			return false;
		if (entity.isInvisible() && invisibles.get())
			return true;
		if (!(entity instanceof EntityPlayer) && onlyplayers.get())
			return true;
		if (entity.isDead)
			return true;
		if (!entity.isEntityAlive())
			return true;
		if (((EntityLivingBase) entity).getHealth() < 0.0F)
			return true;
		if (((EntityLivingBase) entity).deathTime > 0)
			return true;
		if (entity == Minecraft.getMinecraft().thePlayer)
			return true;
		if (entity.getUniqueID() == null)
			return true;
		return false;
	}
}