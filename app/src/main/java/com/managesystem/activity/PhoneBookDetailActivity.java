package com.managesystem.activity;

import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.phoneBooke.FragmentDepartmentList;
import com.managesystem.fragment.phoneBooke.FragmentPhonenumberList;
import com.managesystem.fragment.phoneBooke.PhoneBookDetailDetailFragment;
import com.managesystem.model.Department;
import com.managesystem.model.PersonalInfo;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class PhoneBookDetailActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        PersonalInfo department = (PersonalInfo) getIntent().getExtras().getSerializable("bundle");
        pushFragmentToBackStack(PhoneBookDetailDetailFragment.class, department);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
