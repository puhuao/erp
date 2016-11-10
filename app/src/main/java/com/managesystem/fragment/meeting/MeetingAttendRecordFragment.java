package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.adapter.MeetingAttendRecordAdapter;
import com.managesystem.model.MeetingAttendRecord;
import com.managesystem.widegt.NestedListView;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 参会记录
 */
public class MeetingAttendRecordFragment extends CommonFragment {
    @Bind(R.id.list_view)
    NestedListView listView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    MeetingAttendRecordAdapter adapter;
    ArrayList<MeetingAttendRecord> records = new ArrayList<>();
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
        adapter = new MeetingAttendRecordAdapter(getContext());
        listView.setAdapter(adapter);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        for (int i =0 ;i <10;i ++){
            records.add(new MeetingAttendRecord());
        }
        adapter.setList(records);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().pushFragmentToBackStack(PersonalMeetingDetailFragment.class,null);
            }
        });
    }
}
