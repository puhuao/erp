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
import com.managesystem.model.PPSComment;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
public class MeetingAttendDetailFragment extends CommonFragment {
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

    ArrayList<MeetingApplyRecord> applyRecords = new ArrayList<>();
    private MeetingApplyRecord meetingApplyRecord;
    private MeetingSelectCondition meetingSelectCondition;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_attend_detail, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        meetingSelectCondition = (MeetingSelectCondition) getmDataIn();
        setHeaderTitle("参会详情");
        getMeetings();
    }

    private void bundeDataToView(){
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());
        tvDescription.setText(meetingApplyRecord.getInfor());
    }



    private void getMeetings(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition.setUserId(config.getString("userId", ""));
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",meetingSelectCondition.getPageNo())
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
                        bundeDataToView();
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
