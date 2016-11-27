package com.managesystem.activity;

import android.os.Bundle;

import com.managesystem.R;
import com.managesystem.fragment.ebook.FragmentEBookList;
import com.managesystem.fragment.meeting.MeetingPersonListFragment;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;

public class PersonListActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        pushFragmentToBackStack(MeetingPersonListFragment.class, getIntent().getExtras());
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
