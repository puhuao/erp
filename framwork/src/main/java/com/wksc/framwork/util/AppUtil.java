package com.wksc.framwork.util;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by wanglin on 2015/6/3.
 */
public class AppUtil {

    private static AppUtil mInstance = null;

    private Application mApplication;

    public static void onCreate(Application app) {
        mInstance = new AppUtil(app);
    }

    private AppUtil(Application application) {
        mApplication = application;

        // local display
        LocalDisplay.init(application);

        // network status
        NetworkStatusManager.init(application);
    }

    public static AppUtil getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return mApplication;
    }

    public String getAndroidId() {
        String id = Settings.Secure.getString(mApplication.getContentResolver(), Settings.Secure.ANDROID_ID);
        return id;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    private int SCREEN_WIDTH = 0;


    public int getScreenWidth(Context context) {
        if (SCREEN_WIDTH == 0) {
            WindowManager systemService = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metric = new DisplayMetrics();
            systemService.getDefaultDisplay().getMetrics(metric);
            SCREEN_WIDTH = metric.widthPixels;
        }
        return SCREEN_WIDTH;
    }

}
