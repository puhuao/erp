package com.managesystem.fragment;

import android.app.Notification;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.CustomApplication;
import com.managesystem.R;
import com.managesystem.activity.LoginActivity;
import com.managesystem.activity.MainActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.RemoteVersion;
import com.managesystem.tools.UrlUtils;
import com.managesystem.update.UpdateManager;
import com.managesystem.widegt.SwitchButton;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.AppManager;
import com.wksc.framwork.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 设置页面
 */
public class SettingFragment extends CommonFragment {
    @Bind(R.id.check_version)
    TextView checkVersion;
    @Bind(R.id.phone_public)
    SwitchButton switchButton;
    int currentVersionCode;
    private IConfig config;
    private boolean isSilence;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("设置");
        config = BaseApplication.getInstance().getCurrentConfig();
        isSilence = config.getBoolean("silence", false);
        switchButton.setChecked(isSilence?true:false);
        PackageManager manager = getActivity().getPackageManager();
        try {

            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            currentVersionCode = info.versionCode;
            String versionName = info.versionName;
            checkVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    modify(1);
                }else{
                    modify(0);
                }
            }
        });
    }
    private void modify(int pub) {
        if (pub == 1) {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getContext());
            builder.statusBarDrawable = R.mipmap.ic_launcher;
            builder.notificationFlags = Notification.FLAG_INSISTENT;  //设置为点击后自动消失
            builder.notificationDefaults = Notification.DEFAULT_VIBRATE;  //设置为呼吸灯
            JPushInterface.setPushNotificationBuilder(1,builder);
            config.setBoolean("silence", true);
//            JPushInterface.setSilenceTime(CustomApplication.getContext(), 0, 0, 24, 59);
        } else {
            config.setBoolean("silence", false);
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getContext());
            builder.statusBarDrawable = R.mipmap.ic_launcher;
            builder.notificationFlags = Notification.FLAG_INSISTENT;  //设置为点击后自动消失
            builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为呼吸灯
            JPushInterface.setPushNotificationBuilder(1,builder);
        }
    }

    @OnClick({R.id.logout, R.id.check_version,R.id.re_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_back:
                getContext().pushFragmentToBackStack(ReBackFragment.class,null);
                break;
            case R.id.logout:
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                config.setBoolean("isLogin", false);
                getContext().finish();
                AppManager.getAppManager().finishActivity(MainActivity.class);
                startActivity(LoginActivity.class);
                break;
            case R.id.check_version:
                checkVersion(String.valueOf(currentVersionCode));
                break;

        }
    }

    private void checkVersion(String versionCode) {
        StringBuilder sb = new StringBuilder(Urls.CHECK_VERSION);
        UrlUtils.getInstance(sb).praseToUrl("versionCode", versionCode)
                .praseToUrl("type", "1")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<RemoteVersion>(getContext(), RemoteVersion.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, RemoteVersion o, Request request, @Nullable Response response) {
                if (o != null) {
                     Boolean isNew = o.isIsNew();
                    if (!isNew){
                       RemoteVersion.NewVersionBean newVersionBean =  o.getNewVersion();
                            UpdateManager.getUpdateManager().setContext(getContext());
                            UpdateManager.getUpdateManager().setUpdateInfo(newVersionBean);
                            UpdateManager.getUpdateManager().showNoticeDialog();
                    }
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
