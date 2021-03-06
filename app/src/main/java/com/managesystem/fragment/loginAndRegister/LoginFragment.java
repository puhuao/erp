package com.managesystem.fragment.loginAndRegister;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.CustomApplication;
import com.managesystem.R;
import com.managesystem.activity.ForgetPasswordActivity;
import com.managesystem.activity.MainActivity;
import com.managesystem.activity.RegisterActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.MD5Utils;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by puhua on 2016/5/26.
 *
 * @
 */
public class LoginFragment extends CommonFragment {
    @Bind(R.id.et_username)
    EditText userName;
    @Bind(R.id.et_password)
    EditText passWord;
    @Bind(R.id.checkbox)
    CheckBox checkBox;
    private IConfig config;
    private String username;
    private String password;
    private Boolean isAotuLogin = false;
    public Boolean isSilence = false;
    public final static String TAG = "jpush";
     DialogCallback callback;
    private static final int MSG_SET_ALIAS = 1001;
    private  MyHandler mHandler ;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTitleHeaderBar.setVisibility(View.GONE);
        isActiv = true;
        final View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        config = BaseApplication.getInstance().getCurrentConfig();
        username = config.getString("username", "");
        password = config.getString("password", "");
        isSilence = config.getBoolean("silence", false);
        userName.setText(username);

        if (config.getBoolean("remember", false)) {
            checkBox.setChecked(true);
            passWord.setText(password);
            if (config.getBoolean("isLogin", false)){
                isAotuLogin = true;
            }
        }
        mHandler = new MyHandler(getActivity());
        if (isAotuLogin) {
            doLogin();
        }

        return view;
    }

    @OnClick({R.id.fab, R.id.register_new_user, R.id.tv_forgot_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                doLogin();
                break;
            case R.id.register_new_user:
                startActivity(RegisterActivity.class);
                break;
            case R.id.tv_forgot_password:
                startActivity(ForgetPasswordActivity.class);
                break;

        }
    }


    private void doLogin() {

        username = userName.getText().toString();
        password = passWord.getText().toString();
        if (StringUtils.isBlank(username)) {
            ToastUtil.showShortMessage(getContext(), "请输入用户名");
            return;
        }
        if (StringUtils.isBlank(password)) {
            ToastUtil.showShortMessage(getContext(), "请输入密码");
            return;
        }
        StringBuilder sb = new StringBuilder(Urls.LOGIN);
        UrlUtils.getInstance(sb).praseToUrl("phone", username).praseToUrl("password", MD5Utils.encode(password))
                .removeLastWord();
        callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo o, Request request, @Nullable Response response) {
                if (o != null) {
                    config.setString("username", username);
                    config.setString("userId", o.getUserId());
                    config.setString("password", password);
                    config.setString("roleName", o.getRoleName());
                    config.setString("name", o.getName());
                    config.setString("stationName", o.getStationName());
                    config.setString("department", o.getDepartmentName());
                    config.setString("cphone", o.getCphone());
                    config.setString("headerIcon", Urls.GETPICS + o.getHeadPic());
                    config.setBoolean("isLogin", true);
                    config.setString("sign", o.getSign());
                    config.setString("phone", o.getPhone());
                    config.setString("status",o.getStatus());
                    config.setString("position",o.getPosition()==null?"":o.getPosition());

                    if (o.getIspublish() == 1) {
                        config.setBoolean("ispublish", true);
                    } else {
                        config.setBoolean("ispublish", false);
                    }
                    if (checkBox.isChecked()) {
                        config.setBoolean("remember", true);
                    }else{
                        config.setBoolean("remember", false);
                    }

                    if (o.getStatus().equals("0")){
                        ToastUtil.showShortMessage(getContext(),"账号未激活!");
                    }

                    if (JPushInterface.isPushStopped(getContext().getApplicationContext())){
                        JPushInterface.resumePush(getContext().getApplicationContext());
                    }
                    String alias = o.getUserId();
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

                }

            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    if (getActivity() == null) {
                        return;
                    }
                    callback.getDialog().dismiss();
                    startActivity(MainActivity.class);
                    getActivity().finish();
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 10);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    private Boolean isActiv;

    @Override
    public void onPause() {
        super.onPause();
        isActiv = false;
    }

    private class MyHandler extends Handler{
        private final WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    if (mActivity.get() == null) {
                        return;
                    }
                    if (isActiv){
                        callback.getDialog().show();
                        Log.d(TAG, "Set alias in handler.");
                        // 调用 JPush 接口来设置别名。
                        JPushInterface.setAlias(CustomApplication.getContext(), String.valueOf(msg.obj),
                                mAliasCallback);
                    }
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
