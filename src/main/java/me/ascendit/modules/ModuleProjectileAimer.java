package me.ascendit.modules;

public class ModuleProjectileAimer extends Module
{
	public ModuleProjectileAimer()
	{
		super("Projectile Aimer", "Aims projectiles for you onto a specific block", Category.COMBAT);
		this.registerModule();
	}
	
	@Override
	public void onTick() 
	{
	}

}
