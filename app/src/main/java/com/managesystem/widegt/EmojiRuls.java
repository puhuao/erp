package com.managesystem.widegt;

import android.content.Context;

import com.managesystem.R;

import java.util.HashMap;


/**
 * Created by puhua on 2016/4/7.
 *
 * @
 */
public class EmojiRuls {
    private static Context mContext;

    public static HashMap<String, Integer> map;

    public static String[] describe;

    public static int[] mImageIds = new int[]{R.drawable.smiley_0,
            R.drawable.smiley_1, R.drawable.smiley_2, R.drawable.smiley_3,
            R.drawable.smiley_4, R.drawable.smiley_5, R.drawable.smiley_6,
            R.drawable.smiley_7, R.drawable.smiley_8, R.drawable.smiley_9,
            R.drawable.smiley_10, R.drawable.smiley_11, R.drawable.smiley_12,
            R.drawable.smiley_13, R.drawable.smiley_14, R.drawable.smiley_15,
            R.drawable.smiley_16, R.drawable.smiley_17, R.drawable.smiley_18,
            R.drawable.smiley_19, R.drawable.smiley_20, R.drawable.smiley_21,
            R.drawable.smiley_22, R.drawable.smiley_23, R.drawable.smiley_24,
            R.drawable.smiley_25, R.drawable.smiley_26, R.drawable.smiley_27,
            R.drawable.smiley_28, R.drawable.smiley_29, R.drawable.smiley_30,
            R.drawable.smiley_31, R.drawable.smiley_32, R.drawable.smiley_33,
            R.drawable.smiley_34, R.drawable.smiley_35, R.drawable.smiley_36,
            R.drawable.smiley_37, R.drawable.smiley_38, R.drawable.smiley_39,
            R.drawable.smiley_40, R.drawable.smiley_41, R.drawable.smiley_42,
            R.drawable.smiley_43, R.drawable.smiley_44, R.drawable.smiley_45,
            R.drawable.smiley_46, R.drawable.smiley_47, R.drawable.smiley_48,
            R.drawable.smiley_49, R.drawable.smiley_50, R.drawable.smiley_51,
            R.drawable.smiley_52, R.drawable.smiley_53, R.drawable.smiley_54,
            R.drawable.smiley_55, R.drawable.smiley_56, R.drawable.smiley_57,
            R.drawable.smiley_58, R.drawable.smiley_59, R.drawable.smiley_60,
            R.drawable.smiley_61, R.drawable.smiley_62, R.drawable.smiley_63,
            R.drawable.smiley_64, R.drawable.smiley_65, R.drawable.smiley_66,
            R.drawable.smiley_67, R.drawable.smiley_68, R.drawable.smiley_69,
            R.drawable.smiley_70, R.drawable.smiley_71, R.drawable.smiley_72,
            R.drawable.smiley_73, R.drawable.smiley_74, R.drawable.smiley_75,
            R.drawable.smiley_76, R.drawable.smiley_77, R.drawable.smiley_78,
            R.drawable.smiley_79, R.drawable.smiley_80, R.drawable.smiley_81,
            R.drawable.smiley_82, R.drawable.smiley_83, R.drawable.smiley_84,
            R.drawable.smiley_85, R.drawable.smiley_86, R.drawable.smiley_87,
            R.drawable.smiley_88, R.drawable.smiley_89, R.drawable.smiley_90,
            R.drawable.smiley_91, R.drawable.smiley_92, R.drawable.smiley_93,
            R.drawable.smiley_94, R.drawable.smiley_95, R.drawable.smiley_96,
            R.drawable.smiley_97, R.drawable.smiley_98, R.drawable.smiley_99,
            R.drawable.smiley_100, R.drawable.smiley_101,
            R.drawable.smiley_102, R.drawable.smiley_103, R.drawable.smiley_104};

    public static void getruls(Context context) {
        map = new HashMap<>();
        describe = context.getResources().getStringArray(R.array.qq_emoji_vals);
        for (int i = 0; i < describe.length; i++) {
            map.put(describe[i], mImageIds[i]);
        }
    }
}
