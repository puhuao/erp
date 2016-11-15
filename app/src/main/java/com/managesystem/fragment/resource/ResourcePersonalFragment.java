package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.activity.GoodNewsActivity;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.adapter.ResourcePersonAdapter;
import com.managesystem.fragment.goodnews.GoodNewsSingInFragment;
import com.managesystem.model.GoodNews;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8.
 *我的物资
 */
public class ResourcePersonalFragment extends CommonFragment {
    @Bind(R.id.list_view)
    NestedListView listView;
    ResourcePersonAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    View empty;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        empty = inflater.inflate(R.layout.empty_view, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.resource_manage));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        resourcePersonAdapter = new ResourcePersonAdapter(getContext());
        listView.setAdapter(resourcePersonAdapter);
        ((ViewGroup) (listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        for (int i=0;i<10;i++){
            resourcePersonModels.add(new ResourcePersonModel());
        }
        resourcePersonAdapter.setList(resourcePersonModels);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
            }
        });
    }

}
