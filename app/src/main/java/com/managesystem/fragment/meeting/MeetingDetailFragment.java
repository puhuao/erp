package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.loginAndRegister.ForgetPasswordFragment;
import com.managesystem.fragment.loginAndRegister.RegisterFragment;
import com.managesystem.model.AddUserParam;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.popupwindow.MeetingNoticeNextPersonPopupwindow;
import com.managesystem.popupwindow.MeetingSignPersonPopupwindow;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 会议详情
 */
public class MeetingDetailFragment extends CommonFragment {
    @Bind(R.id.name)
    TextView tvName;
    @Bind(R.id.start_time)
    TextView tvStartTime;
    @Bind(R.id.location)
    TextView tvLocation;
    @Bind(R.id.end_time)
    TextView tvEndTime;
    @Bind(R.id.description)
    TextView tvDescription;
    @Bind(R.id.sign_person)
    View sign;
    @Bind(R.id.attend_person)
    View attend;
    @Bind(R.id.select_next_person)
    Button btnSelectNext;
    @Bind(R.id.notice_all)
    Button btnNoticeAll;


    private MeetingApplyRecord meetingApplyRecord;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_detail, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        meetingApplyRecord = getArguments().getParcelable("key");
        hideTitleBar();
        bundeDataToView();
    }

    @OnClick({R.id.sign_person,R.id.attend_person,R.id.select_next_person,R.id.notice_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice_all:
                noticeAll();
                break;
            case R.id.sign_person:
//                if (meetingApplyRecord.getSignUsers()!=null){
//                    if (meetingApplyRecord.getSignUsers().size()>0){
//                        MeetingSignPersonPopupwindow personPopupwindow = new MeetingSignPersonPopupwindow(getContext()
//                        ,meetingApplyRecord.getSignUsers());
//                        personPopupwindow.showPopupwindow(sign);
//                    }else{
//                        ToastUtil.showShortMessage(getContext(),"没有签到人员");
//                    }
//                }else{
//                    ToastUtil.showShortMessage(getContext(),"没有签到人员");
//                }
                break;
            case R.id.attend_person:
                if (meetingApplyRecord.getSignUsers()!=null){
                    if (meetingApplyRecord.getSignUsers().size()>0){
                        MeetingSignPersonPopupwindow personPopupwindow = new MeetingSignPersonPopupwindow(getContext()
                                ,meetingApplyRecord.getSignUsers());
                        personPopupwindow.showPopupwindow(attend);
                    }else{
                        ToastUtil.showShortMessage(getContext(),"没有出席人员");
                    }
                }else{
                    ToastUtil.showShortMessage(getContext(),"没有出席人员");
                }
                break;
            case R.id.select_next_person:
                if (meetingApplyRecord.getNoticeUsers()!=null){
                    if (meetingApplyRecord.getNoticeUsers().size()>0){
                        MeetingNoticeNextPersonPopupwindow personPopupwindow = new MeetingNoticeNextPersonPopupwindow(getContext()
                                ,meetingApplyRecord.getNoticeUsers());
                        personPopupwindow.showPopupwindow(btnSelectNext);
                    }else{
                        ToastUtil.showShortMessage(getContext(),"没有可通知的人");
                    }
                }else{
                    ToastUtil.showShortMessage(getContext(),"没有可通知的人");
                }
                break;
        }
    }



    private void bundeDataToView(){
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());
        tvDescription.setText(meetingApplyRecord.getInfor());
    }

    private void noticeAll(){
        AddUserParam addUserParam = new AddUserParam(meetingApplyRecord.getMeetingId(),"0","0");
        StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
        UrlUtils.getInstance(sb).praseToUrl("meetingId",addUserParam.getMeetingId())
                .praseToUrl("type","0")
                .praseToUrl("isAll","0")
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
                    ToastUtil.showShortMessage(getContext(),"通知所有用户成功");
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
