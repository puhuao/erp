package com.managesystem.fragment.modify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnDepartmentModifyedEvent;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 设置手机号是否公开
 */
public class ModifyAccountIsPublishFragment extends CommonFragment {
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
        setHeaderTitle("手机号是否公开");
        if (config.getBoolean("ispublish",false)){
            pub = 1;
            isPublish.setChecked(true);
        }else{
            pub = 0;
            isUnPublish.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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



    private void modify(int pub){
        StringBuilder sb = new StringBuilder(Urls.SAVE_USER);
        String s = UrlUtils.getInstance(sb)
                .praseToUrl("userId",config.getString("userId",""))
                .praseToUrl("ispublish",String.valueOf(pub))
                .praseToUrl("type","2")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"修改成功");
                    config.setString("roleName",o.getRoleName());
                    config.setString("name",o.getName());
                    config.setString("stationName",o.getStationName());
                    config.setString("department",o.getDepartmentName());
                    config.setString("cphone",o.getCphone());
                    config.setString("headerIcon",o.getHeadPic());
                    config.setBoolean("isLogin",true);
                    config.setString("phone",o.getPhone());
                    config.setString("sign",o.getSign());
                    if (o.getIspublish()==1){
                        config.setBoolean("ispublish",true);
                    }else{
                        config.setBoolean("ispublish",false);
                    }
                    EventBus.getDefault().post(new OnDepartmentModifyedEvent());
                    getContext().popTopFragment(null);
                }
            }
        };
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }

}
