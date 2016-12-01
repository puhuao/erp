package com.managesystem.fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.fragment.goodnews.GoodNewsDetailFragment;
import com.managesystem.fragment.goodnews.GoodNewsSingInFragment;
import com.managesystem.model.GoodNews;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 我的消费页面
 */
public class MyWalletFragment extends CommonFragment {


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_wallet, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
    }
}
