package com.managesystem.fragment.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.activity.MeetingMsgDetailActivity;
import com.managesystem.adapter.MsgReadAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.Message;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 消息未读
 */
public class MsgNotReadFragment extends BaseListRefreshFragment<Message> {
    MsgReadAdapter msgReadAdapter;
    ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        hideTitleBar();
        msgReadAdapter = new MsgReadAdapter(getContext());
        setData(messages,msgReadAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), MeetingMsgDetailActivity.class);
                Message message = messages.get(position);
                i.putExtra("obj",message);
                getContext().startActivity(i);
            }
        });
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MSG_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("status","0")
                .removeLastWord();
        excute(sb.toString(),Message.class);
    }
}
