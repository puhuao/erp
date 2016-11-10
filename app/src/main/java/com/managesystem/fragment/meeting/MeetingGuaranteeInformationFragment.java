package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 保障信息
 */
public class MeetingGuaranteeInformationFragment extends CommonFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_guarantee, null);
        ButterKnife.bind(this, container);
    initView();
        return container;
    }
    private void initView() {
        hideTitleBar();
    }
}
