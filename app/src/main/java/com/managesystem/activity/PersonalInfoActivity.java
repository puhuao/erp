package com.managesystem.activity;

import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.PersonalInfoFragment;
import com.managesystem.fragment.goodnews.GoodNewsFragment;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class PersonalInfoActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        pushFragmentToBackStack(PersonalInfoFragment.class, null);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
