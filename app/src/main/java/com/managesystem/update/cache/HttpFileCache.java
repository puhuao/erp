package com.managesystem.update.cache;

import android.content.Context;

import java.io.File;

/**
 * Http请求缓存到文件中
 * 
 * @author wanglin@gohighedu.com
 * @date 2013-6-26下午3:11:02
 */
public class HttpFileCache {
    
    private File cacheDir;
    
    public Context currentContext;
    
    public HttpFileCache(Context context) {
        currentContext = context;
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState()
                                  .equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),
                                "ftgcache");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public File getFileFromHash(String url) {
        File f = new File(cacheDir, url);
        
        return f;
        
    }
    
    /**
     * 清除缓存
     */
    public void clear() {
        if(cacheDir!=null&&cacheDir.exists()){
            File[] files = cacheDir.listFiles();
            for (File f : files)
                f.delete();
        }
     
    }
    
}
