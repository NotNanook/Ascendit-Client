package me.ascendit.util;

import java.util.Arrays;

public class StringUtils {
	
	public static String toCompleteString(String[] args, int start) {
        if(args.length <= start)
        	return "";

        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }
	
	public static String toCompleteString(String[] args, int start, int end) {
        if(args.length <= start)
        	return "";

        return String.join(" ", Arrays.copyOfRange(args, start, end));
    }
}