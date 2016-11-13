package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.Users;
import com.managesystem.widegt.RatingBar;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 保障信息
 */
public class MeetingGuaranteeInformationFragment extends CommonFragment {
    @Bind(R.id.name)
    TextView tvName;
    @Bind(R.id.start_time)
    TextView tvStartTime;
    @Bind(R.id.location)
    TextView tvLocation;
    @Bind(R.id.end_time)
    TextView tvEndTime;
    @Bind(R.id.guarantee_progress)
    TextView tvGuaranteeProgress;
    @Bind(R.id.guarantee_person)
    TextView tvGuaranteePerson;
    @Bind(R.id.rating_bar)
    RatingBar ratingBar;
    private MeetingApplyRecord meetingApplyRecord;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_guarantee, null);
        ButterKnife.bind(this, container);
        meetingApplyRecord = getArguments().getParcelable("key");
         initView();
        return container;
    }
    private void initView() {
        hideTitleBar();
        bundeDataToView();
    }

    private void bundeDataToView(){
        ratingBar.setStar(3f);
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());

        switch (meetingApplyRecord.getStatus()){
            case 0:
                tvGuaranteeProgress.setText("新增");
                break;
            case 1:
                tvGuaranteeProgress.setText("已受理");
                break;
            case 2:
                tvGuaranteeProgress.setText("已确认");
                break;
            case 3:
                tvGuaranteeProgress.setText("已完成");
                break;
            case 4:
                tvGuaranteeProgress.setText("已评价");
                break;
        }
        ArrayList<Users> handleUsers = meetingApplyRecord.getHandleUsers();
        StringBuilder sb = new StringBuilder();
        if (handleUsers!=null&&handleUsers.size()>0){
            for (Users user :
                    handleUsers) {
                sb.append(user.getName()).append("、");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        tvGuaranteePerson.setText(sb);
    }
}
