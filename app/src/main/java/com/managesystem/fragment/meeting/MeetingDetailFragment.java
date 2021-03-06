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
import com.managesystem.activity.PersonListActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.AddUserParam;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.popupwindow.MeetingNoticeNextPersonPopupwindow;
import com.managesystem.popupwindow.MeetingSignPersonPopupwindow;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.QRCodeModel;
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

    @OnClick({R.id.btn_sign_in, R.id.btn_sign_up, R.id.sign_person, R.id.attend_person, R.id.select_next_person, R.id.notice_all})
    public void onClick(View v) {
        QRCodeModel qrCodeModel = new QRCodeModel();
        QRChecInModel qrChecInModel = new QRChecInModel();
        switch (v.getId()) {
            case R.id.btn_sign_in:
                //签到二维码
                qrChecInModel.setMeetingId(meetingApplyRecord.getMeetingId());
                qrChecInModel.setType("2");
                qrCodeModel.setType("2");
                qrCodeModel.setParam(qrChecInModel);
//                qrCodeModel.setParam(GsonUtil.objectToJson(qrChecInModel));
                try {
                    Bitmap bitmap = CreateQrCode.createQRCode(GsonUtil.objectToJson(qrCodeModel), 300);
                    QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(), bitmap);
                    popupwindow.showPopupwindow(tvDescription);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_sign_up:
                //报名二维码
//                StringBuilder sb1 = new StringBuilder("meeting/saveUser?");
//                UrlUtils.getInstance(sb1).praseToUrl("meetingId",addUserParam1.getMeetingId())
//                        .praseToUrl("type","1")
//                        .removeLastWord();
                qrChecInModel.setMeetingId(meetingApplyRecord.getMeetingId());
                qrChecInModel.setType("1");
                qrCodeModel.setType("1");
                qrCodeModel.setParam(qrChecInModel);
//                qrCodeModel.setParam(GsonUtil.objectToJson(qrChecInModel));
                try {
                    Bitmap bitmap = CreateQrCode.createQRCode(GsonUtil.objectToJson(qrCodeModel), 300);
                    QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(), bitmap);
                    popupwindow.showPopupwindow(tvDescription);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.notice_all:
                getMeetings(1);
                break;
            case R.id.sign_person:
                getMeetings(2);
                break;
            case R.id.attend_person:
                getMeetings(3);
                break;
            case R.id.select_next_person:
                getMeetings(4);
                break;
        }
    }


    private void bundeDataToView() {
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());
        tvDescription.setText(meetingApplyRecord.getInfor());
    }

    private void noticeAll() {
        AddUserParam addUserParam = new AddUserParam(meetingApplyRecord.getMeetingId(), "0", "0");
        StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
        UrlUtils.getInstance(sb).praseToUrl("meetingId", addUserParam.getMeetingId())
                .praseToUrl("type", "0")
                .praseToUrl("isAll", "1")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "通知所有用户成功");
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void getMeetings(final int i) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", "1")
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("pageSize", "20")
                .praseToUrl("isQueryDetail", "1")
                .praseToUrl("meetingId", meetingApplyRecord.getMeetingId())
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        List<MeetingApplyRecord> list1 = GsonUtil.fromJsonList(list, MeetingApplyRecord.class);
                        if (list1.size() > 0) {
                            meetingApplyRecord = list1.get(0);
                            if (i == 1) {
                                noticeAll();
                            } else if (i == 2) {
                                if (meetingApplyRecord.getApplyUsers() != null) {
                                    if (meetingApplyRecord.getApplyUsers().size() > 0) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("list", meetingApplyRecord.getApplyUsers());
                                        bundle.putInt("type", 0);
                                        startActivity(PersonListActivity.class, bundle);
                                    } else {
                                        ToastUtil.showShortMessage(getContext(), "没有报名人员");
                                    }
                                } else {
                                    ToastUtil.showShortMessage(getContext(), "没有报名人员");
                                }
                            } else if (i == 3) {
                                if (meetingApplyRecord.getSignUsers() != null) {
                                    if (meetingApplyRecord.getSignUsers().size() > 0) {

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("list", meetingApplyRecord.getSignUsers());
                                        bundle.putInt("type", 1);
                                        startActivity(PersonListActivity.class, bundle);
                                    } else {
                                        ToastUtil.showShortMessage(getContext(), "没有参会人员");
                                    }
                                } else {
                                    ToastUtil.showShortMessage(getContext(), "没有参会人员");
                                }
                            } else if (i == 4) {
                                if (meetingApplyRecord.getApplyUsers() != null) {
                                    if (meetingApplyRecord.getApplyUsers().size() > 0) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("list", meetingApplyRecord.getApplyUsers());
                                        bundle.putInt("type", 2);
                                        bundle.putString("meetingId", meetingApplyRecord.getMeetingId());
                                        startActivity(PersonListActivity.class, bundle);
                                    } else {
                                        ToastUtil.showShortMessage(getContext(), "没有可通知的人");
                                    }
                                } else {
                                    ToastUtil.showShortMessage(getContext(), "没有可通知的人");
                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
