package com.managesystem.fragment.loginAndRegister;

import android.app.Notification;
import android.os.Bundle;
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
import com.managesystem.activity.MainActivity;
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

import java.util.Calendar;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
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


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTitleHeaderBar.setVisibility(View.GONE);
        final View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        config = BaseApplication.getInstance().getCurrentConfig();
        username = config.getString("username", "");
        password = config.getString("password", "");
        isSilence = config.getBoolean("silence", false);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
       int minute = c.get(Calendar.MINUTE);
        if (isSilence) {
            JPushInterface.setSilenceTime(CustomApplication.getContext(), hour-1, minute, hour-1, minute);
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getContext());
            builder.statusBarDrawable = R.mipmap.ic_launcher;
            builder.notificationFlags = Notification.FLAG_INSISTENT;  //设置为点击后自动消失
            builder.notificationDefaults = Notification.DEFAULT_LIGHTS;  //设置为呼吸灯
            JPushInterface.setDefaultPushNotificationBuilder(builder);
        } else {
            JPushInterface.setSilenceTime(CustomApplication.getContext(), 0, 0, 0, 0);
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getContext());
            builder.statusBarDrawable = R.mipmap.ic_launcher;
            builder.notificationFlags = Notification.FLAG_INSISTENT;  //设置为点击后自动消失
            builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为呼吸灯
            JPushInterface.setDefaultPushNotificationBuilder(builder);
        }
        userName.setText(username);
        if (config.getBoolean("remember", false)) {
            checkBox.setChecked(true);
            passWord.setText(password);
            if (config.getBoolean("isLogin", false)){
                isAotuLogin = true;
            }
        }
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
                getContext().pushFragmentToBackStack(RegisterFragment.class, null);
                break;
            case R.id.tv_forgot_password:
                getContext().pushFragmentToBackStack(ForgetPasswordFragment.class, null);
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
        final DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

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
                    JPushInterface.setAlias(getContext().getApplicationContext(), o.getUserId(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.i("TAG", "jpush:设置别名成功");
                        }
                    });

                    startActivity(MainActivity.class);
                    getActivity().finish();
                }

            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
