package com.managesystem.popupwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.managesystem.R;


/**
 * Created by Administrator on 2016/5/29.
 */
public class QrcodeViewPopupwindow extends PopupWindow {

    Activity mContext;
    public QrcodeViewPopupwindow(Activity context, Bitmap bitmap){
        super();
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_prcode_view,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                dismiss();
            }
        });
    }
    public void showPopupwindow(View view){
        backgroundAlpha(0.5f);
        showAtLocation(view, Gravity.CENTER,0,0);
        mContext.overridePendingTransition(com.wksc.framwork.R.anim.push_left_in,
                com.wksc.framwork.R.anim.push_left_out);
    }
    public void dissmisPopupwindow(){
        this.dismiss();
        mContext.overridePendingTransition(com.wksc.framwork.R.anim.push_left_in,
                com.wksc.framwork.R.anim.push_left_out);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0

        mContext.getWindow().setAttributes(lp);
    }

}
