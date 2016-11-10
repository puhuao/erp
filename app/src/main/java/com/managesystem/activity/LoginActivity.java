package com.managesystem.activity;

import android.content.Intent;
import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.loginAndRegister.LoginFragment;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class LoginActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //解决release版本安装后点击打开跳转页面，点击home之后再次打开luncher图标重复打开问题
        if(!this.isTaskRoot()) {
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent=getIntent();
            String action=mainIntent.getAction();
            if(mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        setContentView(R.layout.activity_main_container);
        pushFragmentToBackStack(LoginFragment.class, null);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
