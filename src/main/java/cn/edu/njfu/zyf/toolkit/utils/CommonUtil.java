package cn.edu.njfu.zyf.toolkit.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CommonUtil {

	public static boolean isCollectionEmpty(Collection<?> c) {
		return c == null || c.size() == 0;
	}
	
	public static boolean isStringEmpty(CharSequence c) {
		return c == null || c.length() == 0;
	}
	
    public static String mapToPrettyString(Map map) {
        StringBuilder sb = new StringBuilder();
        sb.append('{').append('\n');
        Iterator<Entry> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Entry e = itr.next();
            sb.append('\t').append(e.getKey()).append(" -> ").append(e.getValue()).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
    
}
