package com.managesystem.fragment.goodnews;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.managesystem.R;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.GoodNews;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 好消息_已报名页面
 */
public class GoodNewsSingInFragment extends BaseListRefreshFragment<GoodNews> {
    GoodNewsAdapter goodNewsAdapter;
    ArrayList<GoodNews> goodNewses = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        setHeaderTitle("已报名福利");
        goodNewsAdapter = new GoodNewsAdapter(getContext());
        setData(goodNewses,goodNewsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=goodNewses.size())
                getContext().pushFragmentToBackStack(GoodNewsDetailFragment.class,goodNewses.get(position));
            }
        });
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.GOOD_NEWS);
        String userid = null;
        userid = config.getString("userId", "");
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",userid)
                .praseToUrl("pageSize","20")
                .praseToUrl("type","2")//我的已报名福利
                .removeLastWord();
        excute(sb.toString(),GoodNews.class);
    }
}
