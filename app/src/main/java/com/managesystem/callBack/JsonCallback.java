package com.managesystem.callBack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.LoginActivity;
import com.managesystem.activity.MainActivity;
import com.managesystem.activity.MyWalletActivity;
import com.managesystem.event.GoToComment;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.util.AppManager;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends EncryptCallback<T> {

    private Class<T> clazz;
    private Type type;
    private Context mConext;

    public JsonCallback(Context context,Class<T> clazz) {
        this.clazz = clazz;
        this.mConext = context;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    //该方法是子线程处理，不能做ui相关的工作
    @Override
    public T parseNetworkResponse(Response response) throws Exception {

        Headers headers = response.headers();
       final String cookie = headers.get("set-cookie");
        Map<String,List<String>> map =headers.toMultimap();
        List<String> list = map.get("set-cookie");
        String responseData = response.body().string();
        if (TextUtils.isEmpty(responseData)) return null;

        /**
         * 一般来说，服务器返回的响应码都包含 code，msg，data 三部分，在此根据自己的业务需要完成相应的逻辑判断
         * 以下只是一个示例，具体业务具体实现
         */
        JSONObject jsonObject = new JSONObject(responseData);
        final String msg = jsonObject.optString("msg", "");
        final int code = jsonObject.optInt("status", 0);
        String data = jsonObject.optString("data", "");
        String sessionId = jsonObject.optString("sessionId","");
        IConfig config =  BaseApplication.getInstance().getCurrentConfig();
        if (!StringUtils.isBlank(sessionId)){
            config.setString("sessionId",sessionId);
        }
        switch (code) {
            case 0:
                BaseInfo.code = 0;
                OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortMessage(OkHttpUtils.getContext(),msg);
                    }
                });
                break;
            case 1:
                BaseInfo.code = 1;
                if (clazz == String.class) return (T) data;
                if (clazz != null){
                    T c = new Gson().fromJson(data, clazz);
                    if (c==null)
                    OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortMessage(OkHttpUtils.getContext(),msg);
                        }
                    });
                    return c;
                }
                if (type != null) return new Gson().fromJson(data, type);
                break;
            case 10:
//                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                config.setBoolean("isLogin", false);
                AppManager.getAppManager().finishActivity(MyWalletActivity.class);
                AppManager.getAppManager().finishActivity(MainActivity.class);
                mConext.startActivity(new Intent(mConext, LoginActivity.class));
                OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortMessage(mConext,"用户信息过期,请重新登录");
                    }
                });
                break;
            case 104:
                //比如：用户授权信息无效，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("用户授权信息无效");
            case 105:
                //比如：用户收取信息已过期，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("用户收取信息已过期");
            case 106:
                //比如：用户账户被禁用，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("用户账户被禁用");
            case 300:
                //比如：其他乱七八糟的等，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("其他乱七八糟的等");
            default:
                throw new IllegalStateException("错误代码：" + code + "，错误信息：" + msg);
        }
        //如果要更新UI，需要使用handler，可以如下方式实现，也可以自己写handler
//        OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(OkHttpUtils.getContext(), "错误代码：" + code + "，错误信息：" + msg, Toast.LENGTH_SHORT).show();
//            }
//        });
        return null;
    }
}
