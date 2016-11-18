package com.managesystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.activity.GoodNewsActivity;
import com.managesystem.activity.MeetingManageActivity;
import com.managesystem.activity.ResourceApplyActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MainFragment extends CommonFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.app_name));
        enableDefaultBack(false);
    }

    @OnClick({R.id.layout_meet_apply,R.id.layout_good_news,R.id.layout_resource_apply})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_meet_apply:
                startActivity(MeetingManageActivity.class);
                break;
            case R.id.layout_good_news:
                startActivity(GoodNewsActivity.class);
                break;
            case R.id.layout_resource_apply:
                startActivity(ResourceApplyActivity.class);
                break;
        }
    }
}