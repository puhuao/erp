package com.managesystem.activity;

import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.phoneBooke.FragmentDepartmentList;
import com.managesystem.fragment.phoneBooke.FragmentPhonenumberList;
import com.managesystem.model.Department;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class PhoneBookListActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        Department department = (Department) getIntent().getExtras().getSerializable("bundle");
        pushFragmentToBackStack(FragmentPhonenumberList.class,
                department);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
