package com.managesystem.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by puhua on 2016/12/5.
 *
 * @基本配置信息
 */

public class Constans {
    /** 默认下载文件地址. */
    public static String downPathRootDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "erp"
            + File.separator;
}
