package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.adapter.MeetingApplyRecordAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 申请会议记录
 */
public class MeetingApplyRecordFragment extends BaseListRefreshFragment<MeetingRoomDetail> {
    MeetingApplyRecordAdapter adapter;
    ArrayList<MeetingRoomDetail> records = new ArrayList<>();
    private MeetingSelectCondition meetingSelectCondition;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        meetingSelectCondition = new MeetingSelectCondition();
        hideTitleBar();
        adapter = new MeetingApplyRecordAdapter(getContext());
        setData(records,adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=records.size()){

                    meetingSelectCondition.setMeetingId(records.get(position).getMeetingId());
                    getContext().pushFragmentToBackStack(PersonalMeetingDetailFragment.class,meetingSelectCondition);
                }
            }
        });
    }


    @Override
    public void loadMore(final int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition.setUserId(config.getString("userId", ""));
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",meetingSelectCondition.getUserId())
                .praseToUrl("pageSize",meetingSelectCondition.getPageSize())
                .praseToUrl("meetingName",meetingSelectCondition.getMeetingName())
                .praseToUrl("date",meetingSelectCondition.getDate())
                .removeLastWord();
        excute(sb.toString(),MeetingRoomDetail.class);
    }


    public int getnotComment() {
        int count = 0;
        for (int i = 0 ;i <records.size();i++){
            if (records.get(i).getStatus()==3){
                count++;
            }
        }
        return count;
    }
}
