package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.MeetingNoticePersonAdapter;
import com.managesystem.adapter.PopMeetingRoomAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.Users;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 会议室选择
 */
public class MeetingRoomSelectFragment extends CommonFragment{
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
                MeetingRoomSelectEvent event = new MeetingRoomSelectEvent(list.get(position));
                EventBus.getDefault().post(event);
                getContext().popTopFragment(null);
            }
        });
    }
}
