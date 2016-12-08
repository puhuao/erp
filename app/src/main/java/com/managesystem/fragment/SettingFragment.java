package com.managesystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.activity.LoginActivity;
import com.managesystem.activity.MainActivity;
import com.wksc.framwork.util.AppManager;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8.
 * 设置页面
 */
public class SettingFragment extends CommonFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("设置");
    }

    @OnClick({R.id.logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                config.setBoolean("isLogin",false);
                getContext().finish();
                AppManager.getAppManager().finishActivity(MainActivity.class);
                startActivity(LoginActivity.class);
                break;
        }
        }
}
