package com.managesystem.callBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Window;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.BaseRequest;
import com.managesystem.activity.TransferZxingCaptureActivity;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private ProgressDialog dialog;

    private void initDialog(final Activity activity) {

        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求数据中...");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                OkHttpUtils.getInstance().cancelTag(activity);
            }
        });
    }

    public DialogCallback(TransferZxingCaptureActivity activity, Class<T> clazz) {
        super(activity,activity,clazz);
        initDialog(activity);
    }

    public DialogCallback(Activity activity, Class<T> clazz) {
        super(activity,activity,clazz);
        initDialog(activity);
    }
    public DialogCallback(Activity activity, Class<T> clazz,SwipeRefreshLayout refreshLayout) {
        super(activity,activity,clazz);
        mSwipeRefreshLayout = refreshLayout;
        show = false;
        initDialog(activity);
    }

    public DialogCallback(Activity activity, Type type) {
        super(type);
        initDialog(activity);
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //网络请求前显示对话框
        if (show)
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

        if (mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);
        if (mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private Boolean show = true;
    public void setDialogHide() {
        show = false;
    }
private SwipeRefreshLayout mSwipeRefreshLayout;
    public void setRefreshLayout(SwipeRefreshLayout refreshLayout) {
        this.mSwipeRefreshLayout = refreshLayout;
        show = false;
    }
}
