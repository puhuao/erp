package com.managesystem.fragment.goodnews;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.managesystem.R;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.config.Urls;
import com.managesystem.event.GoodeNewsCheckEvent;
import com.managesystem.event.OnGoodNewsSignIn;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.GoodNews;
import com.managesystem.model.PPSModel;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 好消息页面
 */
public class GoodNewsFragment extends BaseListRefreshFragment<GoodNews> {
    GoodNewsAdapter goodNewsAdapter;
    ArrayList<GoodNews> goodNewses = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_good_news, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        setHeaderTitle(getStringFromResource(R.string.good_news));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.good_news_has_sign_in));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        goodNewsAdapter = new GoodNewsAdapter(getContext());
        setData(goodNewses,goodNewsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=goodNewses.size())
                getContext().pushFragmentToBackStack(GoodNewsDetailFragment.class, goodNewses.get(position));
            }
        });
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(GoodNewsSingInFragment.class,null);
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
                .praseToUrl("type","1")//所有福利
                .removeLastWord();
        excute(sb.toString(),GoodNews.class);
    }

    @Subscribe
    public void onEvent(GoodeNewsCheckEvent event){
        pageNo = 1;
        loadMore(1);

    }
    @Subscribe
    public void onEvent(OnGoodNewsSignIn event){
        pageNo = 1;
        loadMore(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
