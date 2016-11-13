package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.DepartmentSelectEvent;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.model.Department;
import com.managesystem.model.MeetingApply;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingType;
import com.managesystem.popupwindow.DepartmentSelectPopupwindow;
import com.managesystem.popupwindow.MeetingRoomSelectPopupwindow;
import com.managesystem.popupwindow.MeetingTypeSelectPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 申请会议
 */
public class MeetingApplyFragment extends CommonFragment {
    @Bind(R.id.tv_location)
    TextView tvMeetingRoom;
    @Bind(R.id.tv_type)
    TextView tvMeetingType;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_start_time)
    EditText etStartTime;
    @Bind(R.id.et_end_time)
    EditText etEndTime;
    @Bind(R.id.et_description)
    EditText etDescription;

    private ArrayList<MeetingRoom> meetingRooms = new ArrayList<>();
    private ArrayList<MeetingType> meetingTypes = new ArrayList<>();

    private MeetingRoom meetingRoom;
    private MeetingType meetingType;

    private MeetingApply meetingApply;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_apply, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Subscribe
    public void onEvent(MeetingRoomSelectEvent event){
        meetingRoom = event.getDepartment();
        tvMeetingRoom.setText(meetingRoom.getMeetingroomName());
    }

    @Subscribe
    public void onEvent(MeetingTypeSelectEvent event){
        meetingType = event.getDepartment();
        tvMeetingType.setText(meetingType.getServicetypeName());
    }


    @OnClick({R.id.fab,R.id.tv_location,R.id.tv_type})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                meetingApply.setUserId(config.getString("userId", ""));
                meetingApply.setMeetingName(etName.getText().toString());
                if (StringUtils.isBlank(meetingApply.getMeetingName())){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_hint_name));
                    return;
                }
                if (meetingRoom!=null){
                    meetingApply.setMeetingroomId(meetingRoom.getMeetingroomId());
                }else{
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_hint_location));
                    return;
                }

                if (meetingType!=null){
                    meetingApply.setServicetypeId(meetingType.getServicetypeId());
                }else{
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_hint_type));
                    return;
                }
                meetingApply.setStartDate(etStartTime.getText().toString());
                if (StringUtils.isBlank(meetingApply.getStartDate())){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_hint_start_time));
                    return;
                }
                meetingApply.setEndDate(etEndTime.getText().toString());
                if (StringUtils.isBlank(meetingApply.getEndDate())){
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_hint_end_time));
                    return;
                }
                meetingApply.setInfor(etDescription.getText().toString());
                apply();
                break;
            case R.id.tv_location:
                if (meetingRooms.size()>0){
                    MeetingRoomSelectPopupwindow popupwindow = new MeetingRoomSelectPopupwindow(getContext(),meetingRooms);
                    popupwindow.showPopupwindow(tvMeetingRoom);
                }else{
                    getMeetingRooms();
                }
                break;
            case R.id.tv_type:
                if (meetingTypes.size()>0){
                    MeetingTypeSelectPopupwindow  popupwindow = new MeetingTypeSelectPopupwindow(getContext(),meetingTypes);
                    popupwindow.showPopupwindow(tvMeetingType);
                }else{
                    getMeetingTypes();
                }
                break;
        }
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.meeting_apply));
        meetingApply = new MeetingApply();
    }

    private void getMeetingRooms(){
        StringBuilder sb = new StringBuilder(Urls.MEETING_ROOM);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    meetingRooms.addAll(GsonUtil.fromJsonList(o, MeetingRoom.class));
                    MeetingRoomSelectPopupwindow popupwindow = new MeetingRoomSelectPopupwindow(getContext(),meetingRooms);
                    popupwindow.showPopupwindow(tvMeetingRoom);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void getMeetingTypes(){
        StringBuilder sb = new StringBuilder(Urls.METTING_TYPES);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    meetingTypes.addAll(GsonUtil.fromJsonList(o, MeetingType.class));
                    MeetingTypeSelectPopupwindow  popupwindow = new MeetingTypeSelectPopupwindow(getContext(),meetingTypes);
                    popupwindow.showPopupwindow(tvMeetingType);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void apply(){
        StringBuilder sb = new StringBuilder(Urls.MEETIG_APPLY);

       String data = GsonUtil.objectToJson(meetingApply);
        try {
            data = URLEncoder.encode(data,"utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UrlUtils.getInstance(sb) .praseToUrl("data", data)
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
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_apply_success));
                    getContext().popTopFragment(null);
                }
            }
        };

        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
