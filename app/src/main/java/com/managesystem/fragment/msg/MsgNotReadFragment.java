package com.managesystem.fragment.msg;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.adapter.MsgReadAdapter;
import com.managesystem.model.Message;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 消息未读
 */
public class MsgNotReadFragment extends CommonFragment {
    @Bind(R.id.list_view)
    NestedListView listView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    MsgReadAdapter msgReadAdapter;
    ArrayList<Message> messages = new ArrayList<>();
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
        hideTitleBar();
        msgReadAdapter = new MsgReadAdapter(getContext());
//        for (int i =0;i<6;i++){
//            messages.add(new Message());
//        }
        listView.setAdapter(msgReadAdapter);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        msgReadAdapter.setList(messages);
    }
}
