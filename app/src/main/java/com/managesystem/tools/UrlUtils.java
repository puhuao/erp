package com.managesystem.tools;

import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;

/**
 * Created by Administrator on 2016/7/2.
 */
public class UrlUtils {


    private static UrlUtils mInstance;

    public StringBuilder base;

    private UrlUtils(StringBuilder base){
        this.base = base;
    }

    public static UrlUtils getInstance(StringBuilder base) {
        if (mInstance == null) {
            synchronized (UrlUtils.class) {
                if (mInstance == null) {
                    mInstance = new UrlUtils(base);
                }
            }
        }
        mInstance.base = base;
        return mInstance;
    }
    public UrlUtils praseToUrl( String key, String value) {
        if (!StringUtils.isBlank(value))
            mInstance.base.append(key).append("=").append(value).append("&");
        return this;
    }
    public String removeLastWord() {
        if (!StringUtils.isBlank(mInstance.base.toString()))
            if ((mInstance.base.charAt(mInstance.base.length()-1)=='&')){
                mInstance.base.deleteCharAt(mInstance.base.length()-1);
            }
        return mInstance.base.toString();
    }

}
