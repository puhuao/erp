package com.managesystem.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.managesystem.R;
import com.managesystem.fragment.pps.PPSDetailFragment;
import com.managesystem.fragment.pps.PublishPPSFragment;
import com.managesystem.model.PPSModel;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class PPSDetailActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main_container);
        PPSModel ppsModel = (PPSModel) getIntent().getExtras().getSerializable("item");
        pushFragmentToBackStack(PPSDetailFragment.class, ppsModel);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
