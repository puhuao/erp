package com.managesystem.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.CustomApplication;
import com.managesystem.R;
import com.managesystem.activity.LoginActivity;
import com.managesystem.activity.MainActivity;
import com.managesystem.fragment.modify.ModifySilenceFragment;
import com.wksc.framwork.util.AppManager;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/8.
 * 设置页面
 */
public class SettingFragment extends CommonFragment {
    @Bind(R.id.setting)
    TextView silence;
    @Bind(R.id.check_version)
    TextView checkVersion;

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
        if (isSilence){
            silence.setText("开启");
        } else {
            silence.setText("关闭");
        }
        PackageManager manager = getActivity().getPackageManager();
        try {
//            int remoteVersionCode = Integer.valueOf(o.version);
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            int versionCode = info.versionCode;
            String versionName = info.versionName;
//                        ToastUtil.showShortMessage(getActivity(), "versonCode=" + remoteVersionCode + " thisVer"
//                                + versionCode);

//            if (remoteVersionCode > versionCode) {
//                UpdateManager.getUpdateManager().setContext(getContext());
//                UpdateManager.getUpdateManager().setUpdateInfo(o);
//                UpdateManager.getUpdateManager().showNoticeDialog();
//            }
            checkVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.logout,R.id.check_version,R.id.setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                config.setBoolean("isLogin",false);
                getContext().finish();
                AppManager.getAppManager().finishActivity(MainActivity.class);
                startActivity(LoginActivity.class);
                break;
            case  R.id.setting:
                getContext().pushFragmentToBackStack(ModifySilenceFragment.class,null);
                break;
            case R.id.check_version:
                break;

        }
        }
}
