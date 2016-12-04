package com.managesystem.activity;

import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.ebook.FragmentDepartmentList;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class EBookActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        pushFragmentToBackStack(com.managesystem.fragment.phoneBooke.FragmentDepartmentList.class, null);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
