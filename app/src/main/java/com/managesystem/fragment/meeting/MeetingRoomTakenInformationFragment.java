package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.Users;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 会议室占用情况
 */
public class MeetingRoomTakenInformationFragment extends CommonFragment {
    @Bind(R.id.name)
    TextView tvName;
    @Bind(R.id.start_time)
    TextView tvStartTime;
    @Bind(R.id.location)
    TextView tvLocation;
    @Bind(R.id.end_time)
    TextView tvEndTime;

    @Bind(R.id.apply_person)
    TextView tvApplyPerson;
    @Bind(R.id.apply_person_area)
    TextView tvApplyPersonArea;
    @Bind(R.id.apply_person_department)
    TextView tvApplyPersonDepartment;
    @Bind(R.id.apply_person_phone)
    TextView tvApplyPersonPhone;
    @Bind(R.id.apply_person_floor)
    TextView tvApplyPersonFloor;
    @Bind(R.id.apply_person_office)
    TextView tvApplyPersonOffice;

    private MeetingSelectCondition meetingSelectCondition;
    private MeetingApplyRecord meetingApplyRecord;
    ArrayList<MeetingApplyRecord> applyRecords = new ArrayList<>();
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_room_taken, null);
        ButterKnife.bind(this, container);
        meetingSelectCondition = (MeetingSelectCondition) getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        getMeetings();
    }

    private void getMeetings(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition.setUserId(config.getString("username", ""));
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",meetingSelectCondition.getPageNo())
//                .praseToUrl("userId",meetingSelectCondition.getUserId())
                .praseToUrl("pageSize",meetingSelectCondition.getPageSize())
                .praseToUrl("meetingName",meetingSelectCondition.getMeetingName())
                .praseToUrl("date",meetingSelectCondition.getDate())
                .praseToUrl("isQueryDetail","1")
                .praseToUrl("meetingId",meetingSelectCondition.getMeetingId())
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");

                        applyRecords.addAll(GsonUtil.fromJsonList(list,MeetingApplyRecord.class));
                        meetingApplyRecord = applyRecords.get(0);
                        bindView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void bindView(){
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());
        ArrayList<Users> applyUsers = meetingApplyRecord.getApplyUsers();
        StringBuilder sb = new StringBuilder();
        if (applyUsers!=null&&applyUsers.size()>0){
            for (Users user :
                    applyUsers) {
                sb.append(user.getName()).append("、");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        tvApplyPerson.setText(sb);
        tvApplyPersonArea.setText(meetingApplyRecord.getArea());
        tvApplyPersonDepartment.setText(meetingApplyRecord.getDepartmentName());
        tvApplyPersonPhone.setText(meetingApplyRecord.getCphone());
       tvApplyPersonFloor.setText(meetingApplyRecord.getFloor());
         tvApplyPersonOffice.setText(meetingApplyRecord.getOfficeNo());
    }
}
