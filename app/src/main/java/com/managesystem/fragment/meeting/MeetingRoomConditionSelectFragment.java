package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.managesystem.R;
import com.managesystem.adapter.PopMeetingRoomAdapter;
import com.managesystem.event.MeetingRoomConditionSelectEvent;
import com.managesystem.model.MeetingRoom;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 会议室选择
 */
public class MeetingRoomConditionSelectFragment extends CommonFragment{
    @Bind(R.id.list_view)
    ListView listView ;
    @Bind(R.id.fab)
    Button fab;
    PopMeetingRoomAdapter adapter;
    private List<MeetingRoom> list;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.pop_department_select, null);
        ButterKnife.bind(this, container);
        list = (List<MeetingRoom>)getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        adapter = new PopMeetingRoomAdapter(getContext());
        setHeaderTitle("会议室选择");
        listView.setAdapter(adapter);
        adapter.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingRoomConditionSelectEvent event = new MeetingRoomConditionSelectEvent(list.get(position));
                EventBus.getDefault().post(event);
                getContext().popTopFragment(null);
            }
        });
    }
}
