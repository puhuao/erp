package com.managesystem.activity;

import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.goodnews.GoodNewsFragment;
import com.managesystem.fragment.maintain.MainTainApplyFragment;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class MainTainApplyActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        pushFragmentToBackStack(MainTainApplyFragment.class, getIntent().getExtras());
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
