package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.activity.GoodNewsActivity;
import com.managesystem.activity.MeetingManageActivity;
import com.managesystem.fragment.goodnews.GoodNewsSingInFragment;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8.
 *
 */
public class ResourceManageFragment extends CommonFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_manage, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.resource_manage));
        hideLeftButton();
    }

    @OnClick({R.id.rl_resource_person,R.id.rl_resource_apply,R.id.rl_resource_lost,R.id.rl_resource_yard})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_resource_person:
                getContext().pushFragmentToBackStack(ResourcePersonalFragment.class,null);
                break;
            case R.id.rl_resource_apply:
                break;
            case R.id.rl_resource_lost:
                getContext().pushFragmentToBackStack(PersonalResourceLostFragment.class,null);
                break;
            case R.id.rl_resource_yard:
                startActivity(GoodNewsActivity.class);
                break;
        }
    }
}
