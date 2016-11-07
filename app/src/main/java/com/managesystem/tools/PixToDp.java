package com.managesystem.tools;

import android.content.Context;

/**
 * Created by Administrator on 2016/7/13.
 */
public class PixToDp {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
