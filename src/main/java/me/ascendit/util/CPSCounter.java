package me.ascendit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CPSCounter  {
	// a mixin in Minecraft.class is used here

	private static List<Long> leftcps = new ArrayList<Long>();
	private static List<Long> rightcps = new ArrayList<Long>();

	public static int getLeftCPS() {
		Iterator<Long> iterator = leftcps.iterator();
	    while (iterator.hasNext()) {
	      if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
	        iterator.remove(); 
	    } 
	    return leftcps.size();
	}
	
	public static void addLeftClick() {
	    leftcps.add(Long.valueOf(System.currentTimeMillis()));
	}

	public static int getRightCPS() {
		Iterator<Long> iterator = rightcps.iterator();
	    while (iterator.hasNext()) {
	      if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
	        iterator.remove(); 
	    }
	    return rightcps.size();
	}

	public static void addRightClick() {
	    rightcps.add(Long.valueOf(System.currentTimeMillis()));
	}
}