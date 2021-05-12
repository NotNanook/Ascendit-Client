package me.ascendit.module.modules.render;

import java.awt.Font;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.ascendit.Ascendit;
import me.ascendit.event.EventTarget;
import me.ascendit.event.events.KeyEvent;
import me.ascendit.event.events.Render2DEvent;
import me.ascendit.module.Category;
import me.ascendit.module.Module;
import me.ascendit.module.ModuleInfo;
import me.ascendit.module.ModuleManager;
import me.ascendit.setting.BoolSetting;
import me.ascendit.setting.DoubleSetting;
import me.ascendit.setting.FloatSetting;
import me.ascendit.setting.IntSetting;
import me.ascendit.setting.ModeSetting;
import me.ascendit.setting.Setting;
import me.ascendit.ui.font.FontRenderer;
import me.ascendit.util.CPSCounter;
import me.ascendit.util.ColorUtils;
import me.ascendit.util.MovementUtils;
import me.ascendit.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "HUD", description = "Head-up display", category = Category.RENDER)
public class HUD extends Module {

	private final BoolSetting clientname = new BoolSetting("ClientName", true);
	private final BoolSetting tabgui = new BoolSetting("TabGUI", true);
	public  final BoolSetting customfont = new BoolSetting("Custom Font", false);
	public  final BoolSetting customhotbar = new BoolSetting("CustomHotbar", true);
	private final BoolSetting backround = new BoolSetting("Backround", true);
	private final BoolSetting informationhud = new BoolSetting("InformationHUD", true);
	private final BoolSetting coords = new BoolSetting("Coords", true);
	private final BoolSetting fps = new BoolSetting("FPS", true);
	private final BoolSetting date = new BoolSetting("Date", true);
	private final BoolSetting time = new BoolSetting("Time", true);
	private final BoolSetting cps = new BoolSetting("CPS", true);
	private final BoolSetting ping = new BoolSetting("Ping", true);
	private final BoolSetting speed = new BoolSetting("Speed", true);
	private final BoolSetting armorhud = new BoolSetting("ArmorHUD", true);
	public  final BoolSetting playermodel = new BoolSetting("PlayerModel", true);

	private final FontRenderer fontrenderer = new FontRenderer("Comfortaa", Font.PLAIN, 20);
	private final FontRenderer fontrendererbold = new FontRenderer("Comfortaa", Font.BOLD, 20);
	private final FontRenderer fontrendererbig = new FontRenderer("Comfortaa", Font.BOLD, 40);
	private int width, height, categoryindex;
	private float textWidth;
	private boolean chatopen;
	private final ArrayList<Module> modules = new ArrayList<Module>();

	public HUD() {
		addSettings(clientname, tabgui, customfont, customhotbar, backround, informationhud, coords, fps, date, time, cps, ping, speed, armorhud, playermodel);
	}

	@EventTarget
	public void onRender2D(final Render2DEvent event) {
		if (mc.gameSettings.showDebugInfo)
			return;
		
		width = event.getResolution().getScaledWidth();
		height = event.getResolution().getScaledHeight();
		
		/*
		 * Client Name
		 */
		if (clientname.get()) {
			if (customfont.get()) {
				fontrendererbig.drawStringWithShadow(Ascendit.MODNAME, 5, 5, 0xFFFF0000);
				fontrendererbold.drawStringWithShadow(Ascendit.VERSION, 86, 15, 0xFFFF0000);
			} else {
				GL11.glPushMatrix();
    			GL11.glScalef(2F, 2F, 2F);
    			GL11.glTranslatef(2, 5, 0);
				mc.fontRendererObj.drawStringWithShadow(Ascendit.MODNAME, 0, 0, 0xFFFF0000);
				GL11.glPopMatrix();
				mc.fontRendererObj.drawStringWithShadow(Ascendit.VERSION, 91, 18, 0xFFFF0000);
			}
		}
		
		/*
		 * TabGUI
		 */
		// Categories
		if (tabgui.get()) {
			int categorycount = 0;
			RenderUtils.drawRect(5, 31.5, 80, 30 + Category.values().length*16 + 2.5, 0x60000000);
			RenderUtils.drawRect(5, 31.5 + categoryindex*16, 80, 28.5 + categoryindex*16 + 20, 0xFFFF0000);
			
			for (final Category category: Category.values()) {
				if (customfont.get()) {
					fontrenderer.drawString(category.name, 10, 36 + categorycount*16 - 2.5F, -1);
					fontrenderer.drawString((Category.values()[categorycount].categoryexpanded ? "<" : ">"), 70, 36 + categorycount*16 - 2.5F, -1);
				} else {
					mc.fontRendererObj.drawString(category.name, 10, 36 + categorycount*16, -1);
					mc.fontRendererObj.drawString((Category.values()[categorycount].categoryexpanded ? "<" : ">"), 70, 36 + categorycount*16, -1);
				}
				
				categorycount++;
			}
			
			// Modules
			int modulecount = 0;
			if (Category.values()[categoryindex].categoryexpanded) {
				final Category currentcategory = Category.values()[categoryindex];
				final List<Module> modulesofcurrentcategory = Module.getModulesbyCategory(currentcategory);
				
				RenderUtils.drawRect(80, 31.5, 185, 30 + modulesofcurrentcategory.size() * 16 + 2.5, 0x60000000);
				RenderUtils.drawRect(80, 31.5 + currentcategory.moduleindex*16, 185, 28.5 + currentcategory.moduleindex*16 + 20, 0xFFFF0000);
				
				for (final Module module: modulesofcurrentcategory) {
					if (customfont.get())
						fontrenderer.drawString(module.getName(), 85, 36 + modulecount*16 - 2.5F, (module.isToggled() ? 0xA0A0A0 : -1));
					else
						mc.fontRendererObj.drawString(module.getName(), 85, 36 + modulecount*16, (module.isToggled() ? 0xA0A0A0 : -1));
					
					if (!module.getSettings().isEmpty()) {
						if (customfont.get())
							fontrenderer.drawString((module.moduleexpanded ? "<" : ">"), 175, 36 + modulecount*16 - 2.5F, -1);
						else
							mc.fontRendererObj.drawString((module.moduleexpanded ? "<" : ">"), 175, 36 + modulecount*16, -1);
					}
					
					// Settings
					if (modulecount == currentcategory.moduleindex && module.moduleexpanded) {
						int settingcount = 0;
						
						if (!module.getSettings().isEmpty()) {
							RenderUtils.drawRect(80 + 105, 31.5, 185 + 125, 30 + module.getSettings().size() * 16 + 2.5, 0x60000000);
							RenderUtils.drawRect(80 + 105, 31.5 + module.settingindex*16, 185 + 125, 28.5 + module.settingindex*16 + 20, 0xFFFF0000);
							
							for (final Setting setting: module.getSettings()) {
								if (!module.getSettings().isEmpty()) {
									if (setting instanceof BoolSetting) {
										final BoolSetting boolsetting = (BoolSetting) setting;
										if (customfont.get())
											fontrenderer.drawString(setting.getName() + ": " + String.valueOf(boolsetting.get()), 85 + 105, 36 + settingcount*16 - 2.5F, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
										else
											mc.fontRendererObj.drawString(setting.getName() + ": " + String.valueOf(boolsetting.get()), 85 + 105, 36 + settingcount*16, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
									} else if (setting instanceof DoubleSetting) {
										final DoubleSetting doublesetting = (DoubleSetting) setting;
										if (customfont.get())
											fontrenderer.drawString(setting.getName() + ": " + doublesetting.get(), 85 + 105, 36 + settingcount*16 - 2.5F, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
										else
											mc.fontRendererObj.drawString(setting.getName() + ": " + doublesetting.get(), 85 + 105, 36 + settingcount*16, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
									} else if (setting instanceof FloatSetting) {
										final FloatSetting floatsetting = (FloatSetting) setting;
										if (customfont.get())
											fontrenderer.drawString(setting.getName() + ": " + floatsetting.get(), 85 + 105, 36 + settingcount*16 - 2.5F, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
										else
											mc.fontRendererObj.drawString(setting.getName() + ": " + floatsetting.get(), 85 + 105, 36 + settingcount*16, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
									} else if (setting instanceof IntSetting) {
										final IntSetting intsetting = (IntSetting) setting;
										if (customfont.get())
											fontrenderer.drawString(setting.getName() + ": " + intsetting.get(), 85 + 105, 36 + settingcount*16 - 2.5F, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
										else
											mc.fontRendererObj.drawString(setting.getName() + ": " + intsetting.get(), 85 + 105, 36 + settingcount*16, (module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1));
									} else if (setting instanceof ModeSetting) {
										final ModeSetting modesetting = (ModeSetting) setting;
										if (customfont.get())
											fontrenderer.drawString(setting.getName() + ": " + modesetting.getMode().toUpperCase(), 85 + 105, 36 + settingcount*16 - 2.5F, module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1);
										else
											mc.fontRendererObj.drawString(setting.getName() + ": " + modesetting.getMode().toUpperCase(), 85 + 105, 36 + settingcount*16, module.getSettings().get(settingcount).focused ? 0xA0A0A0 : -1);
									}
								}
								settingcount++;
							}
						}
					}
					modulecount++;
				}
			}
		}
		
		/*
		 * Module List
		 */
		int textY = 1;
		final int textHeight = 9;
		int modulecount = 0;
		
		for (final Module module: ModuleManager.modules) {
			if (module.isToggled() && !modules.contains(module))
				modules.add(module);
			else if (!module.isToggled())
				modules.remove(module);
		}
		
		// Sort Modules
		if (customfont.get())
			modules.sort((module1, module2) -> (int) fontrenderer.getWidth(module2.getDisplayName()) - (int) fontrenderer.getWidth(module1.getDisplayName()));
		else
			modules.sort((module1, module2) -> (int) mc.fontRendererObj.getStringWidth(module2.getDisplayName()) - (int) mc.fontRendererObj.getStringWidth(module1.getDisplayName()));
		
		for (final Module module: modules) {
			if (customfont.get())
				textWidth = fontrenderer.getWidth(module.getDisplayName());
			else
				textWidth = mc.fontRendererObj.getStringWidth(module.getDisplayName());
			
			if (backround.get()) {
				RenderUtils.drawRect(width - (textWidth + 1) - 8, modulecount*(mc.fontRendererObj.FONT_HEIGHT + 4), width - textWidth - 7, 4 + textHeight + modulecount*(mc.fontRendererObj.FONT_HEIGHT + 4), ColorUtils.getRainbowWave(modulecount*300));
				RenderUtils.drawRect(width - (textWidth + 1) - 6, modulecount*(mc.fontRendererObj.FONT_HEIGHT + 4), width, 4 + textHeight + modulecount*(mc.fontRendererObj.FONT_HEIGHT + 4), 0xc0000000);
			}
			
			if (customfont.get())
				fontrenderer.drawString(module.getDisplayName(), width - (textWidth + 1), textY - 1.5F, ColorUtils.getRainbowWave(modulecount*300));
			else
				mc.fontRendererObj.drawString(module.getDisplayName(), (int) (width - (textWidth + 1)), textY, ColorUtils.getRainbowWave(modulecount*300));
			
			modulecount++;
			textY += textHeight + 4;
		}
		
		/*
		 * Information HUD
		 */
		if (chatopen == false && Keyboard.isKeyDown(mc.gameSettings.keyBindChat.getKeyCode()) && !mc.isGamePaused())
			chatopen = true;
		if (chatopen == true && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_RETURN))
			chatopen = false;
		
		if (informationhud.get()) {
			if (coords.get() && chatopen == false) {
				if (customfont.get())
					fontrenderer.drawString("X: " + (int) mc.thePlayer.posX + " Y: " + (int) mc.thePlayer.posY + " Z: " + (int) mc.thePlayer.posZ, 2, height - mc.fontRendererObj.FONT_HEIGHT - 4, -1);
				else
					mc.fontRendererObj.drawString("X: " + (int) mc.thePlayer.posX + " Y: " + (int) mc.thePlayer.posY + " Z: " + (int) mc.thePlayer.posZ, 2, height - mc.fontRendererObj.FONT_HEIGHT, -1);
			}
			
			if (fps.get() && chatopen == false) {
				if (customfont.get())
					fontrenderer.drawString("FPS: " + String.valueOf(Minecraft.getDebugFPS()), 2, height - mc.fontRendererObj.FONT_HEIGHT - 11 - 4, -1);
				else
					mc.fontRendererObj.drawString("FPS: " + String.valueOf(Minecraft.getDebugFPS()), 2, height - mc.fontRendererObj.FONT_HEIGHT - 11, -1);
			}
			
			if (date.get()) {
				if (customfont.get())
					fontrenderer.drawString("Date: " + String.format("%02d", LocalDateTime.now().getDayOfMonth()) + "/" + String.format("%02d", LocalDateTime.now().getMonthValue()) + "/" + String.format("%02d", LocalDateTime.now().getYear()), width - fontrenderer.getWidth("Date: " + String.format("%02d", LocalDateTime.now().getDayOfMonth()) + "/" + String.format("%02d", LocalDateTime.now().getMonthValue()) + "/" + String.format("%02d", LocalDateTime.now().getYear())), height - mc.fontRendererObj.FONT_HEIGHT - 4, -1);
				else
					mc.fontRendererObj.drawString("Date: " + String.format("%02d", LocalDateTime.now().getDayOfMonth()) + "/" + String.format("%02d", LocalDateTime.now().getMonthValue()) + "/" + String.format("%02d", LocalDateTime.now().getYear()), width - mc.fontRendererObj.getStringWidth("Date: " + String.format("%02d", LocalDateTime.now().getDayOfMonth()) + "/" + String.format("%02d", LocalDateTime.now().getMonthValue()) + "/" + String.format("%02d", LocalDateTime.now().getYear())), height - mc.fontRendererObj.FONT_HEIGHT, -1);
			}
			
			if (time.get())	{
				if (customfont.get())
					fontrenderer.drawString("Time: " + String.format("%02d", LocalDateTime.now().getHour()) + ":" + String.format("%02d", LocalDateTime.now().getMinute()) + ":" + String.format("%02d", LocalDateTime.now().getSecond()),width - fontrenderer.getWidth("Time: " + String.format("%02d", LocalDateTime.now().getHour()) + ":" + String.format("%02d", LocalDateTime.now().getMinute()) + ":" + String.format("%02d", LocalDateTime.now().getSecond())), height - mc.fontRendererObj.FONT_HEIGHT - 11 - 4, -1);
				else
					mc.fontRendererObj.drawString("Time: " + String.format("%02d", LocalDateTime.now().getHour()) + ":" + String.format("%02d", LocalDateTime.now().getMinute()) + ":" + String.format("%02d", LocalDateTime.now().getSecond()),width - mc.fontRendererObj.getStringWidth("Time: " + String.format("%02d", LocalDateTime.now().getHour()) + ":" + String.format("%02d", LocalDateTime.now().getMinute()) + ":" + String.format("%02d", LocalDateTime.now().getSecond())), height - mc.fontRendererObj.FONT_HEIGHT - 11, -1);
			}
			
			if (cps.get()) {
				if (customfont.get())
					fontrenderer.drawStringWithShadow(CPSCounter.getLeftCPS() + " | " + CPSCounter.getRightCPS(), width - 2 - fontrenderer.getWidth(CPSCounter.getLeftCPS() + " | " + CPSCounter.getRightCPS()), height - mc.fontRendererObj.FONT_HEIGHT - 42, -1);
				else
					mc.fontRendererObj.drawStringWithShadow(CPSCounter.getLeftCPS() + " | " + CPSCounter.getRightCPS(), width - 6 - fontrenderer.getWidth(CPSCounter.getLeftCPS() + " | " + CPSCounter.getRightCPS()), height - mc.fontRendererObj.FONT_HEIGHT - 38, -1);
			}
			
			if (ping.get() && chatopen == false && mc.thePlayer != null && mc.theWorld != null && mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null) {
				if (customfont.get())
					fontrenderer.drawString(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + " ms", 55, height - mc.fontRendererObj.FONT_HEIGHT - 11 - 4, -1);
				else
					mc.fontRendererObj.drawString(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + " ms", 55, height - mc.fontRendererObj.FONT_HEIGHT - 11, -1);
			}
			
			if (speed.get()) {
				if (customfont.get())
					fontrenderer.drawStringWithShadow(MovementUtils.getCurrentSpeed() + " blocks/sec",width - fontrenderer.getWidth(MovementUtils.getCurrentSpeed() + " blocks/sec") - 2, height - mc.fontRendererObj.FONT_HEIGHT - 26 - 4, -1);
				else
					mc.fontRendererObj.drawString(MovementUtils.getCurrentSpeed() + " blocks/sec",width - mc.fontRendererObj.getStringWidth(MovementUtils.getCurrentSpeed() + " blocks/sec"), height - mc.fontRendererObj.FONT_HEIGHT - 26, -1, true);
			}
		}
		
		/*
		 * Armor HUD
		 */
		if (armorhud.get()) {
			renderItemStack(mc.thePlayer.getCurrentEquippedItem(), 2, height/2 - 48);
			for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
				final ItemStack itemstack = mc.thePlayer.inventory.armorInventory[i];
				renderArmorItem(i, itemstack);
			}
		}
		
		/*
		 * Player Model
		 */
		if (playermodel.get())
			GuiInventory.drawEntityOnScreen(width - 34, height - 60, 50, 15, 5, mc.thePlayer);
	}

	@EventTarget
	public void onKey(final KeyEvent event) {
		final Category currentcategory = Category.values()[categoryindex];
		final List<Module> modulesofcurrentcategory = Module.getModulesbyCategory(currentcategory);
		final Module currentmodule = modulesofcurrentcategory.get(currentcategory.moduleindex);

		if (tabgui.get() && !mc.gameSettings.showDebugInfo) {
			if (event.getKey() == Keyboard.KEY_UP) {
				if (Category.values()[categoryindex].categoryexpanded) {
					if (currentmodule.moduleexpanded) {
						if (currentmodule.getSettings().get(currentmodule.settingindex).focused) {
							final Setting setting = currentmodule.getSettings().get(currentmodule.settingindex);
							if (setting.focused) {
								if (setting instanceof DoubleSetting) {
									final DoubleSetting doublesetting = (DoubleSetting) setting;
									doublesetting.increment(true);
								}
								if (setting instanceof FloatSetting) {
									final FloatSetting floatsetting = (FloatSetting) setting;
									floatsetting.increment(true);
								}
								if (setting instanceof IntSetting) {
									final IntSetting intsetting = (IntSetting) setting;
									intsetting.increment(true);
								}
							}
						}
						else if (!currentmodule.getSettings().get(currentmodule.settingindex).focused) {
							if (currentmodule.settingindex <= 0) {
								currentmodule.settingindex = currentmodule.getSettings().size() - 1;
							} else {
								currentmodule.settingindex--;
							}
						}
					} else {
						if (currentcategory.moduleindex <= 0) {
							currentcategory.moduleindex = modulesofcurrentcategory.size() - 1;
						} else {
							currentcategory.moduleindex--;
						}
					}

				} else {
					if (categoryindex <= 0) {
						categoryindex = Category.values().length - 1;
					} else {
						categoryindex--;
					}
				}
			}

			if (event.getKey() == Keyboard.KEY_DOWN) {
				if (Category.values()[categoryindex].categoryexpanded) {
					if (currentmodule.moduleexpanded) {
						if (!currentmodule.getSettings().get(currentmodule.settingindex).focused) {
							if (currentmodule.settingindex >= currentmodule.getSettings().size() - 1) {
								currentmodule.settingindex = 0;
							} else {
								currentmodule.settingindex++;
							}
						}
						else {
							final Setting setting = currentmodule.getSettings().get(currentmodule.settingindex);
							if (setting.focused) {
								if (setting instanceof IntSetting) {
									final IntSetting intsetting = (IntSetting) setting;
									intsetting.increment(false);
								}
								if (setting instanceof DoubleSetting) {
									final DoubleSetting doublesetting = (DoubleSetting) setting;
									doublesetting.increment(false);
								}
								if (setting instanceof FloatSetting) {
									final FloatSetting floatsetting = (FloatSetting) setting;
									floatsetting.increment(false);
								}
							}
						}
					} else {
						if (currentcategory.moduleindex >= modulesofcurrentcategory.size() - 1) {
							currentcategory.moduleindex = 0;
						} else {
							currentcategory.moduleindex++;
						}
					}
				} else {
					if (categoryindex >= Category.values().length - 1) {
						categoryindex = 0;
					} else {
						categoryindex++;
					}
				}
			}

			if (event.getKey() == Keyboard.KEY_RIGHT) {
				if (Category.values()[categoryindex].categoryexpanded) {
					if (!currentmodule.moduleexpanded && !currentmodule.getSettings().isEmpty()) {
						currentmodule.moduleexpanded = true;
					} else if (currentmodule.moduleexpanded)
						currentmodule.getSettings().get(currentmodule.settingindex).focused = !currentmodule.getSettings().get(currentmodule.settingindex).focused;
				} else {
					Category.values()[categoryindex].categoryexpanded = true;
				}
			}

			if (event.getKey() == Keyboard.KEY_LEFT) {
				if (Category.values()[categoryindex].categoryexpanded) {
					if (currentmodule.moduleexpanded) {
						if (!currentmodule.getSettings().get(currentmodule.settingindex).focused) {
							currentmodule.moduleexpanded = false;
						} else if (currentmodule.getSettings().get(currentmodule.settingindex).focused) {
							currentmodule.getSettings().get(currentmodule.settingindex).focused = !currentmodule.getSettings().get(currentmodule.settingindex).focused;
						}
					} else {
						Category.values()[categoryindex].categoryexpanded = false;
					}
				}
			}

			if (event.getKey() == Keyboard.KEY_RETURN) {
				if (Category.values()[categoryindex].categoryexpanded) {
					if (!currentmodule.moduleexpanded) {
						if (!currentmodule.getName().equalsIgnoreCase("HUD"))
							currentmodule.toggle();
					}
					else if (currentmodule.moduleexpanded) {
						final Setting setting = currentmodule.getSettings().get(currentmodule.settingindex);
						if (setting.focused) {
							if (setting instanceof BoolSetting) {
								final BoolSetting boolsetting = (BoolSetting) setting;
								if (!boolsetting.getName().equalsIgnoreCase("tabgui"))
									boolsetting.toggle();
							}
							if (setting instanceof ModeSetting) {
								final ModeSetting modesetting = (ModeSetting) setting;
								modesetting.cycle();
							}
						}
					}
				}
			}
		}
	}

	private void renderArmorItem(final int i, final ItemStack itm) {
		if (itm == null)
			return;

		GL11.glPushMatrix();
		final int yoffset = (-16 * i) + 48;

		if (itm.getItem().isDamageable()) {
			final double damage = ((itm.getMaxDamage() - itm.getItemDamage()) / (double) itm.getMaxDamage()) * 100;
			mc.fontRendererObj.drawString(String.format("%.2f%%", damage), 22, height/2 + yoffset - 30, -1);
		}

		RenderHelper.enableGUIStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(itm, 2, height/2 + yoffset - 35);
		GL11.glPopMatrix();
	}

	private void renderItemStack(final ItemStack itm, final int posX, final int posY) {
		if (itm == null)
			return;

		GL11.glPushMatrix();
		if (itm.getItem().isDamageable()) {
			final double damage = ((itm.getMaxDamage() - itm.getItemDamage()) / (double) itm.getMaxDamage()) * 100;
			mc.fontRendererObj.drawString(String.format("%.2f%%", damage), posX + 20, posY, -1);
		}

		RenderHelper.enableGUIStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(itm, posX, posY - 5);
		GL11.glPopMatrix();
	}
}