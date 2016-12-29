package com.managesystem;

import com.wksc.framwork.BaseApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/12.
 */
public class CustomApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashReport.initCrashReport(getApplicationContext(), "e1738fbf7f", true);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

}
