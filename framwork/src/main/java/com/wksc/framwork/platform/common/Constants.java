package com.wksc.framwork.platform.common;

import java.util.regex.Pattern;

/**
 * Defines various constants
 * Created by Gou Zhuang <gouzhuang@gmail.com> on 2014-11-10.
 */
public interface Constants {
    String PREFS_KEY_JPUSH_REGISTRATION_ID = "pmm.jpush_registration_id";
    String PREFS_KEY_AUTO_LOGIN = "settings.auto_login";
    String PREFS_KEY_PREFIX_LATEST_CLUE_ID = "latest_clue_id.";
    String PREFS_KEY_PREFIX_LATEST_CLUE_TIME = "latest_clue_time.";
    String BUNDLE_EXTRA_DEVICE_ADDRESS = "ble.device.address";
    Pattern MOBILE_PHONE_NUMBER_PATTERN = Pattern.compile("1[1-9][0-9]{9}");
    String PARAM_TARGET_ID = "TargetID";
    String PARAM_INDICATE_ID = "IndicateID";
    String PARAM_ANNOUNCE_ID = "AnnounceID";
    String PARAM_INDICATE_DISPLAY_TYPE = "IndicateDisplayType";
}
