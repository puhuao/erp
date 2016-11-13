package com.managesystem.fragment.goodnews;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.model.GoodNews;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 好消息_已报名页面
 */
public class GoodNewsSingInFragment extends CommonFragment {
    @Bind(R.id.list_view)
    NestedListView listView;
    GoodNewsAdapter goodNewsAdapter;
    ArrayList<GoodNews> goodNewses = new ArrayList<>();
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
        setHeaderTitle(getStringFromResource(R.string.good_news));
        goodNewsAdapter = new GoodNewsAdapter(getContext());
        listView.setAdapter(goodNewsAdapter);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        for (int i =0 ;i < 10;i ++){
            goodNewses.add(new GoodNews());
        }
        goodNewsAdapter.setList(goodNewses);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().pushFragmentToBackStack(GoodNewsDetailFragment.class,null);
            }
        });
    }
}
