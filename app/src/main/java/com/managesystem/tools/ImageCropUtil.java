package com.managesystem.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by puhua on 2016/12/5.
 *
 * @图片裁剪工具
 */

public class ImageCropUtil {
    public static String FILE_SAVEPATH = ImageUtils.downPathRootDir + "Portrait" + "/";
    private String protraitPath;
    private File protraitFile;

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    public void startActionCrop(Activity activity, Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = PicSelectUtil.getPath(activity, data);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(data, "image/*");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUploadTempFile(activity,data)); //保存图片到指定uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);// 是否保留比例
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        activity.startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    /**
     * 裁剪头像的绝对路径
     *
     *
     * @param activity
     * @param uri
     * @return
     */
    private Uri getUploadTempFile(Activity activity, Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            ToastUtil.showShortMessage(activity,"无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date());
        //		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);
        String thePath = PicSelectUtil.getPath(activity, uri);
        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(activity, uri);
        }
        String ext = thePath.substring(thePath.lastIndexOf(".") + 1);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;

        // 照片命名
        String cropFileName = "crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        Uri cropUri = Uri.fromFile(protraitFile);
        return cropUri;
    }
}
