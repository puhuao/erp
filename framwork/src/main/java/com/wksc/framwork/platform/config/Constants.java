package com.wksc.framwork.platform.config;

import java.util.regex.Pattern;

/**
 * Created by wanglin on 2015/6/4.
 */
public final class Constants {

    public static final int TIME_COUNT_ALARM = 30000;

    public static final int TIME_COUNT_FUTURE = 60000;

    public static final int TIME_COUNT_INTERVAL = 1000;

    /**tab tag name config*/
    public static final String TAG_COMMUNITY = "community";
    public static final String TAG_SAFETY = "satety";
    public static final String TAG_HEALTHY = "healthy";
    public static final String TAG_NEWS = "news";
    public static final String TAG_CENTER = "center";

    /** prefs keys **/
    public static final String PREFS_KEY_USER_LOGIN_ID = "settings.user_login_id";
    public static final String PREFS_KEY_USER_PASSWORD = "settings.user_password";

    /** patterns **/
    public static final String WEIGHT_PATTERN_STRING = "(\\d+(:?\\.\\d*)?)(\\s*(?i:kg))?";
    public static final String HEIGHT_PATTERN_STRING = "(\\d+)(\\s*(?i:cm))?";
    public static final Pattern WEIGHT_PATTERN = Pattern.compile(WEIGHT_PATTERN_STRING);
    public static final Pattern HEIGHT_PATTERN = Pattern.compile(HEIGHT_PATTERN_STRING);


    /**fragment skip keys*/
    public static final String SKIP_FROM_MEMBER_MANAGER = "skip_from_manager";
    public static final String SKIP_FROM_MEMBER_ALLOCATION = "skip_from_allocation";
    public static final String SKIP_FROM_MEMBER_VO = "member_vo";


    /**settings status keys*/
    public static final String ENABLE_SOUND = "enable_sound";
    public static final String ENABLE_VABRIATE = "enable_vibrate";

    public static final String KEY_TARGET = "key_target";
    public static final String KEY_LOCATION = "key_location";
}
