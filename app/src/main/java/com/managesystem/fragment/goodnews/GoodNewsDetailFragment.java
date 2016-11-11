package com.managesystem.fragment.goodnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 好消息_详情页面
 */
public class GoodNewsDetailFragment extends CommonFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_good_new_detail, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.good_news));
    }
}
