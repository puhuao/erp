package com.managesystem.update.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Http请求缓存到内存中
 * 
 * @author wanglin@gohighedu.com
 * @date 2013-6-26下午3:12:08
 */
public class HttpMemoryCache {
    
    private HashMap<String, SoftReference<String>> cache = new HashMap<String, SoftReference<String>>();
    
    public String get(String id) {
        if (!cache.containsKey(id))
            return null;
        SoftReference<String> ref = cache.get(id);
        return ref.get();
    }
    
    public void put(String id, String content) {
        cache.put(id, new SoftReference<String>(content));
    }
    
    public void clear() {
        if (cache != null && !cache.isEmpty())
            cache.clear();
    }
    
}
