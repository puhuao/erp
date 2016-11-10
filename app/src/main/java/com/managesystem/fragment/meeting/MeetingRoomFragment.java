package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.managesystem.R;
import com.managesystem.adapter.HorizontalListViewAdapter;
import com.managesystem.adapter.MeetingRoomRecordAdapter;
import com.managesystem.model.HorizontalCalenderModel;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.widegt.HorizontalListView;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MeetingRoomFragment extends CommonFragment {
    @Bind(R.id.horizontal_list_view)
    HorizontalListView horizontalListView;
    @Bind(R.id.list_view)
    ListView listView;
    View empty;
    HorizontalListViewAdapter adapter;
    MeetingRoomRecordAdapter meetingRoomRecordAdapter;
    ArrayList<HorizontalCalenderModel> models = new ArrayList<>();
    ArrayList<MeetingRoomDetail> details = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_room, null);
        empty = inflater.inflate(R.layout.empty_view, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        adapter = new HorizontalListViewAdapter(getContext());
        for (int i =0;i<14;i++){
            models.add(new HorizontalCalenderModel());
            details.add(new MeetingRoomDetail());
        }
        horizontalListView.setAdapter(adapter);
        adapter.setList(models);

        meetingRoomRecordAdapter = new MeetingRoomRecordAdapter(getContext());
        listView.setAdapter(meetingRoomRecordAdapter);
        meetingRoomRecordAdapter.setList(details);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().pushFragmentToBackStack(MeetingRoomTakenInformationFragment.class,null);
            }
        });
    }

}
