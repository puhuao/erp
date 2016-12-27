package com.managesystem;

import com.tencent.bugly.crashreport.CrashReport;
import com.wksc.framwork.BaseApplication;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/11/12.
 */
public class CustomApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "e1738fbf7f", false);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //设置别名指定向某个手机发送
        //设置标签发送给某些手机
        Set<String> tagList = new HashSet<>();
        tagList.add("treebear");
        tagList.add("witown");
        tagList.add("tt");
        JPushInterface.setTags(this, tagList, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
    }


}
