package com.managesystem.fragment.meeting;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.AddUserParam;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.popupwindow.MeetingNoticeNextPersonPopupwindow;
import com.managesystem.popupwindow.MeetingSignPersonPopupwindow;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;

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

    @OnClick({R.id.btn_sign_in,R.id.btn_sign_up,R.id.sign_person,R.id.attend_person,R.id.select_next_person,R.id.notice_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                AddUserParam addUserParam = new AddUserParam(meetingApplyRecord.getMeetingId(),"0","0");
                StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
                UrlUtils.getInstance(sb).praseToUrl("meetingId",addUserParam.getMeetingId())
                        .praseToUrl("type","2")
                        .removeLastWord();
                try {
                    Bitmap bitmap = CreateQrCode.createQRCode(sb.toString(), 300);
                    QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(),bitmap);
                    popupwindow.showPopupwindow(tvDescription);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_sign_up:
                AddUserParam addUserParam1 = new AddUserParam(meetingApplyRecord.getMeetingId(),"0","0");
                StringBuilder sb1 = new StringBuilder(Urls.MEETING_ADD_USERS);
                UrlUtils.getInstance(sb1).praseToUrl("meetingId",addUserParam1.getMeetingId())
                        .praseToUrl("type","1")
                        .removeLastWord();
                try {
                    Bitmap bitmap = CreateQrCode.createQRCode(sb1.toString(), 300);
                    QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(),bitmap);
                    popupwindow.showPopupwindow(tvDescription);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
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
