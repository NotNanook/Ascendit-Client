package me.ascendit.module;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.ascendit.event.EventTarget;
import me.ascendit.event.events.KeyEvent;

import me.ascendit.module.modules.combat.*;
import me.ascendit.module.modules.misc.*;
import me.ascendit.module.modules.movement.*;
import me.ascendit.module.modules.player.*;
import me.ascendit.module.modules.render.*;

public class ModuleManager {
	
	public static final	ArrayList<Module> 	modules 			= new ArrayList<Module>();
	
	// Combat
    public static final AutoClicker 		autoclicker 		= new AutoClicker();
    public static final AutoSoup			autosoup			= new AutoSoup();
    public static final BowAimer 			bowaimer 			= new BowAimer();
    public static final Criticals			criticals			= new Criticals();
    public static final HitBox				hitbox				= new HitBox();
    public static final KillAura			killaura			= new KillAura();
    public static final Reach				reach				= new Reach();
    public static final SuperKnockback		superknockback		= new SuperKnockback();
    public static final Velocity			velocity			= new Velocity();
   
    // Miscellaneous
    public static final AntiBot				antibot				= new AntiBot();
    public static final EasyHiveBed 		easyhivebed 		= new EasyHiveBed();
    public static final FakeCheater 		fakecheater 		= new FakeCheater();
    public static final InfiniteMode		infinitemode		= new InfiniteMode();
    public static final InvTracker			invtracker			= new InvTracker();
    public static final NoPitchLimit		nopitchlimit		= new NoPitchLimit();
    
    // Movement
    public static final AirJump				airjump				= new AirJump();
    public static final AutoMLG 			automlg 			= new AutoMLG();
    public static final BugUp 				bugup 				= new BugUp();
    public static final Fly 				fly 				= new Fly();
    public static final Freecam 			freecam 			= new Freecam();
    public static final Freeze 				freeze 				= new Freeze();
    public static final HighJump			highjump			= new HighJump();
    public static final NoFall 				nofall 				= new NoFall();
    public static final NoJumpDelay			nojumpdelay			= new NoJumpDelay();
    public static final NoSlow				noslow				= new NoSlow();
    public static final NoWeb				noweb				= new NoWeb();
    public static final Speed				speed				= new Speed();
    public static final Sprint 				sprint 				= new Sprint();
    public static final Timer				timer				= new Timer();
    
    // Player
    public static final Derp				derp				= new Derp();
    public static final Eagle				eagle				= new Eagle();
    public static final Fastplace 			fastplace 			= new Fastplace();
    public static final FastRespawn 		fastrespawn 		= new FastRespawn();
    public static final Fucker				fucker				= new Fucker();
    public static final InventoryHelper		invhelper			= new InventoryHelper();
    public static final LiquidPlace			liquidplace			= new LiquidPlace();
    public static final MoreCarry			morecarry			= new MoreCarry();
    public static final Teams				teams				= new Teams();
    
    // Render
    public static final AntiBlind			antiblind			= new AntiBlind();
    public static final BlockOverlay		blockoverlay		= new BlockOverlay();
    public static final CameraClip 			cameraclip 			= new CameraClip();
    public static final Chams				chams				= new Chams();
    public static final ESP 				esp 				= new ESP();
    public static final Fullbright 			fullbright 			= new Fullbright();
    public static final HUD 				hud 				= new HUD();
    public static final NoHurtCam 			nohurtcam 			= new NoHurtCam();
    public static final NoScoreBoard		noscoreboard		= new NoScoreBoard();
    public static final NoSwing				noswing				= new NoSwing();
    public static final Perspective			perspective			= new Perspective();
    public static final Projectiles			projectiles			= new Projectiles();
    public static final Rotations			rotations			= new Rotations();
    public static final Truesight 			truesight 			= new Truesight();
    public static final XRay				xray				= new XRay();
    
	@EventTarget
	public void onKey(final KeyEvent event) {
		for (final Module module : ModuleManager.modules) {
			if (module.getKeybind() == Keyboard.getEventKey() && Keyboard.getEventKeyState()) {
				module.toggle();
			}
		}
	}
}