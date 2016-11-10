package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/5.
 * 申请会议
 */
public class MeetingApplyFragment extends CommonFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_apply, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                break;
        }
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.meeting_apply));
    }
}
