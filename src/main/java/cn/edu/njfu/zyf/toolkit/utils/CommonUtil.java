package cn.edu.njfu.zyf.toolkit.utils;

import java.util.Collection;

public class CommonUtil {

	public static boolean isCollectionEmpty(Collection c) {
		return c == null || c.size() == 0;
	}
	
	public static boolean isStringEmpty(CharSequence c) {
		return c == null || c.length() == 0;
	}
}
