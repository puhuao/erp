package com.managesystem.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.managesystem.R;
import com.managesystem.fragment.pps.PPSListFragment;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class PPSActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main_container);
        int type = getIntent().getExtras().getInt("type");
        pushFragmentToBackStack(PPSListFragment.class, type);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
