package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.adapter.MeetingAttendRecordAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.MeetingAttendRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 参会记录
 */
public class MeetingAttendRecordFragment extends BaseListRefreshFragment<MeetingAttendRecord> {
    MeetingAttendRecordAdapter adapter;
    ArrayList<MeetingAttendRecord> records = new ArrayList<>();
    private MeetingSelectCondition meetingSelectCondition;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        hideTitleBar();
        meetingSelectCondition = new MeetingSelectCondition();
        adapter = new MeetingAttendRecordAdapter(getContext());
        setData(records,adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().pushFragmentToBackStack(PersonalMeetingDetailFragment.class,meetingSelectCondition);
            }
        });
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition.setUserId(config.getString("userId", ""));
        StringBuilder sb = new StringBuilder(Urls.MEETING_ATTEND);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",meetingSelectCondition.getUserId())
                .praseToUrl("pageSize",meetingSelectCondition.getPageSize())
//                .praseToUrl("meetingName",meetingSelectCondition.getMeetingName())
//                .praseToUrl("date",meetingSelectCondition.getDate())
                .removeLastWord();
        excute(sb.toString(),MeetingAttendRecord.class);
    }
}
