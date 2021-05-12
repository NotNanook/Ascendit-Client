package me.ascendit.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class ColorUtils {

	public static int getRainbow() {
		float hue = (System.currentTimeMillis() % 15000) / 15000F;
		int color = Color.HSBtoRGB(hue, 1, 1);
		return color;
	}

	public static int getRainbowWave(int delay) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState / 360.0F), 1F, 1F).getRGB();
	}

	public static float[] getRainbow3F() {
		float hue = (System.currentTimeMillis() % 15000) / 15000F;
		Color c = Color.getHSBColor(hue, 1F, 1F);
	    float r = c.getRed() / 255.0F;
	    float g = c.getGreen() / 255.0F;
	    float b = c.getBlue() / 255.0F;

		return new float[] {r, g, b};
	}
	
	public static void glColor(final int red, final int green, final int blue, final int alpha) {
		GL11.glColor4f(red / 255F, green / 255F, blue / 255F, alpha / 255F);
	}
	
	public static void glColor(final Color color) {
		glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public static void glColor(final int hex) {
		glColor(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, hex >> 24 & 0xFF);
	}
}