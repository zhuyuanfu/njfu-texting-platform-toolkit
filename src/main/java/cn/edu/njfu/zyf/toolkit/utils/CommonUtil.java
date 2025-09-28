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
    	
    	int maxKeyLength = 0;
    	Iterator keyIterator = map.keySet().iterator();
    	while(keyIterator.hasNext()) {
    		int keyLength = keyIterator.next().toString().length();
    		if(keyLength > maxKeyLength) {
    			maxKeyLength = keyLength;
    		}
    	}
    	
        StringBuilder sb = new StringBuilder();
        sb.append('{').append('\n');
        Iterator<Entry> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Entry e = itr.next();
            String key = e.getKey().toString();
            String value = e.getValue().toString();
            StringBuilder keySuffixSpaceBuilder = new StringBuilder();
            int keySuffixSpaceCount = maxKeyLength - key.length();
            for(int i = 0; i < keySuffixSpaceCount; i++) {
            	keySuffixSpaceBuilder.append(' ');
            }
            
            sb.append("  ").append(key).append(keySuffixSpaceBuilder.toString()).append(" -> ").append(value).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
    
}
