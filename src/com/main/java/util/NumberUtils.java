package com.main.java.util;

public class NumberUtils {
	private NumberUtils() {
		
	}
	public static Integer zeroIfNull(Integer value) {
		return value == null ? 0 : value;
	}
}
