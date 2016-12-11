package com.managesystem.fragment.modify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.CustomApplication;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnDepartmentModifyedEvent;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 设置手机号是否静音
 */
public class ModifySilenceFragment extends CommonFragment {
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.is_publish)
    RadioButton isPublish;
    @Bind(R.id.is_un_publish)
    RadioButton isUnPublish;
    @Bind(R.id.fab)
    Button fab;

    private IConfig config;
    int pub;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_modify_account_puhlish, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("是否静音");
        isPublish.setText("是");
        isUnPublish.setText("否");
        if (config.getBoolean("silence", false)) {
            pub = 1;
            isPublish.setChecked(true);
        } else {
            pub = 0;
            isUnPublish.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.is_publish:
                        pub = 1;
                        break;
                    case R.id.is_un_publish:
                        pub = 0;
                        break;
                }
            }
        });
    }


    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                modify(pub);
                break;
        }
    }


    private void modify(int pub) {
        if (pub == 1) {
            config.setBoolean("silence", true);
            JPushInterface.setSilenceTime(CustomApplication.getContext(), 0, 0, 24, 59);
        } else {
            config.setBoolean("ispublish", false);
            JPushInterface.setSilenceTime(CustomApplication.getContext(),0,0,0,0);
        }
    }

}
