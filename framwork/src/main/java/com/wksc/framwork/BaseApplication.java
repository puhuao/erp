package com.wksc.framwork;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cookie.store.PersistentCookieStore;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.platform.config.PreferenceConfig;
import com.wksc.framwork.platform.config.PropertiesConfig;
import com.wksc.framwork.util.AppUtil;

/**
 * Created by puhua on 2016/5/26.
 *
 * @
 */
public class BaseApplication extends Application {
    private static final String LOGTAG = BaseApplication.class.getSimpleName();
    private static BaseApplication sInstance;
    private static Context sContext = null;
    private static SharedPreferences sPrefs = null;

    /**
     * 配置器为preference
     */
    public final static int PREFERENCECONFIG = 0;

    /**
     * 配置器为Properties属性文件
     */
    public final static int PROPERTIESCONFIG = 1;

    /**
     * 配置器
     */
    private IConfig mCurrentConfig;


    private boolean isUserLogged;

    private SQLiteDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOGTAG, "Application created");

        sInstance = this;
        sContext = getApplicationContext();
        sPrefs = PreferenceManager.getDefaultSharedPreferences(sContext);

        AppUtil.onCreate(this);

        // Map API must be initialized before inflating map view
        // GeoMapFactory.initGeoMap("Baidu", sContext);

        // start ControlCenterService
//        startService(new Intent(sContext, ControlCenterService.class));

//        RequestManager.init(this);
//必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
//                .setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                .setCookieStore(new PersistentCookieStore());                      //cookie持久化存储，如果cookie不过期，则一直有效
        //init DBManger
        this.initDB();
    }


    private void initDB() {
//        DBManager.getInstance(this).init();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        //MultiDex.install(this);
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferences() {
        return sPrefs;
    }

    public IConfig getConfig(int confingType) {
        if (confingType == PREFERENCECONFIG) {
            mCurrentConfig = PreferenceConfig.getPreferenceConfig(this);

        } else if (confingType == PROPERTIESCONFIG) {
            mCurrentConfig = PropertiesConfig.getPropertiesConfig(this);
        } else {
            mCurrentConfig = PropertiesConfig.getPropertiesConfig(this);
        }
        if (!mCurrentConfig.isLoadConfig()) {
            mCurrentConfig.loadConfig();
        }
        return mCurrentConfig;
    }


    /**
     * 获得Preference配置
     *
     * @return
     */
    public IConfig getPreferenceConfig() {
        return getConfig(PREFERENCECONFIG);
    }

    /**
     * 获得属性文件Properties配置
     *
     * @return
     */
    public IConfig getPropertiesConfig() {
        return getConfig(PROPERTIESCONFIG);
    }

    /**
     * 获得当前配置：默认为PrefrenceConfig
     *
     * @return
     */
    public IConfig getCurrentConfig() {
        if (mCurrentConfig == null) {
            getPreferenceConfig();
        }
        return mCurrentConfig;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }


    public boolean isUserLogged() {
        return isUserLogged;
    }

    public void setUserLogged(boolean isUserLogged) {
        this.isUserLogged = isUserLogged;
    }

}
