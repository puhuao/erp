package com.managesystem;

import android.app.Notification;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

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
       setSounds(this);
    }

    public static void setSounds(Context context){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        boolean isSilence = config.getBoolean("silence", false);
        if (isSilence) {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
            builder.statusBarDrawable = R.mipmap.ic_launcher;
            builder.notificationFlags = Notification.FLAG_INSISTENT;  //设置为点击后自动消失
            builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
            JPushInterface.setPushNotificationBuilder(1,builder);
            JPushInterface.setDefaultPushNotificationBuilder(builder);
        } else {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
            builder.statusBarDrawable = R.mipmap.ic_launcher;
            builder.notificationFlags = Notification.FLAG_INSISTENT;  //设置为点击后自动消失
            builder.notificationDefaults = Notification.DEFAULT_SOUND;
            JPushInterface.setDefaultPushNotificationBuilder(builder);
            JPushInterface.setPushNotificationBuilder(1,builder);
        }
    }

}
