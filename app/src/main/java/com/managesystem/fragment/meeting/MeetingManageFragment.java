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
 * 会议管理
 */
public class MeetingManageFragment extends CommonFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_manage, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.meeting_manage));
    }

    @OnClick({R.id.meeting_occupy,R.id.layout_meeting_apply,R.id.layout_meeting_my})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meeting_occupy:
                //会议室占用
                getContext().pushFragmentToBackStack(MeetingRoomFragment.class,null);
                break;
            case R.id.layout_meeting_apply:
                //会议申请
                getContext().pushFragmentToBackStack(MeetingApplyFragment.class,null);
                break;
            case R.id.layout_meeting_my:
                //我的会议
                getContext().pushFragmentToBackStack(PersonalMeetingFragment.class,null);
                break;

        }
    }
}
