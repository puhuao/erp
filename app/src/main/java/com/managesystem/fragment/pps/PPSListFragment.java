package com.managesystem.fragment.pps;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.activity.MeetingMsgDetailActivity;
import com.managesystem.adapter.MsgReadAdapter;
import com.managesystem.adapter.PPSAdapter;
import com.managesystem.config.Urls;
import com.managesystem.event.PPSListUpdateEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.Message;
import com.managesystem.model.PPSModel;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 论坛列表页面
 */
public class PPSListFragment extends BaseListRefreshFragment<PPSModel> {
    public static int ALL = 0;
    public static int MY = 1;
    PPSAdapter msgReadAdapter;
    ArrayList<PPSModel> messages = new ArrayList<>();
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        type = (int) getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        setHeaderTitle("意见交流");
        getTitleHeaderBar().setRightText("发布新消息");
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        msgReadAdapter = new PPSAdapter(getContext());
        setData(messages,msgReadAdapter);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(PublishPPSFragment.class, null);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().pushFragmentToBackStack(PPSDetailFragment.class,messages.get(position));
            }
        });
    }

    @Subscribe
    public void onEvent(PPSListUpdateEvent event){
        handler.sendEmptyMessage(0);
    }
    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_LIST);
        String userid = null;
        if (type == MY){
            userid = config.getString("userId", "");
        }
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",userid)
                .praseToUrl("pageSize","20")
                .removeLastWord();
        excute(sb.toString(),PPSModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
