package com.managesystem.fragment.meeting;

import android.content.DialogInterface;
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
import com.managesystem.event.GoToComment;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.model.MeetingApply;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingType;
import com.managesystem.popupwindow.MeetingRoomSelectPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.managesystem.widegt.pickview.TimePickerView;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.managesystem.widegt.pickview.view.WheelTime.dateFormat;

/**
 * Created by Administrator on 2016/11/5.
 * 申请会议
 */
public class MeetingApplyFragment extends CommonFragment {
    @Bind(R.id.tv_location)
    TextView tvMeetingRoom;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_start_time)
    TextView etStartTime;
    @Bind(R.id.et_end_time)
    TextView etEndTime;
    @Bind(R.id.et_description)
    EditText etDescription;

    private ArrayList<MeetingRoom> meetingRooms = new ArrayList<>();
    private ArrayList<MeetingType> meetingTypes = new ArrayList<>();

    private MeetingRoom meetingRoom;
    private MeetingType meetingType;

    private MeetingApply meetingApply;
    private TimePickerView timePickerView;

    public int viewId;

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
    public void onEvent(MeetingRoomSelectEvent event) {
        meetingRoom = event.getDepartment();
        tvMeetingRoom.setText(meetingRoom.getArea());
    }

    @Subscribe
    public void onEvent(GoToComment goToComment) {
    }

    @OnClick({R.id.fab, R.id.tv_location, R.id.et_start_time, R.id.et_end_time})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_start_time:
                timePickerView.show();
                viewId = R.id.et_start_time;
                break;
            case R.id.et_end_time:
                viewId = R.id.et_end_time;
                timePickerView.show();
                break;
            case R.id.fab:
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                meetingApply.setUserId(config.getString("userId", ""));
                meetingApply.setMeetingName(etName.getText().toString());
                if (StringUtils.isBlank(meetingApply.getMeetingName())) {
                    ToastUtil.showShortMessage(getContext(), getStringFromResource(R.string.meeting_hint_name));
                    return;
                }
                if (meetingRoom != null) {
                    meetingApply.setMeetingroomId(meetingRoom.getMeetingroomId());
                } else {
                    ToastUtil.showShortMessage(getContext(), getStringFromResource(R.string.meeting_hint_location));
                    return;
                }
                meetingApply.setStartDate(etStartTime.getText().toString());
                if (StringUtils.isBlank(meetingApply.getStartDate())) {
                    ToastUtil.showShortMessage(getContext(), getStringFromResource(R.string.meeting_hint_start_time));
                    return;
                }
                meetingApply.setEndDate(etEndTime.getText().toString());
                if (StringUtils.isBlank(meetingApply.getEndDate())) {
                    ToastUtil.showShortMessage(getContext(), getStringFromResource(R.string.meeting_hint_end_time));
                    return;
                }

                try {
                    Date start = dateFormat.parse(meetingApply.getStartDate());
                    Date end = dateFormat.parse(meetingApply.getEndDate());
                    if (start.after(end)) {
                        ToastUtil.showShortMessage(getContext(), "开始时间不能再结束时间之后");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                meetingApply.setInfor(etDescription.getText().toString());
                apply();
                break;
            case R.id.tv_location:
                if (meetingRooms.size() > 0) {
                    hideSoftInput(v);
                    getContext().pushFragmentToBackStack(MeetingRoomSelectFragment.class, meetingRooms);
                } else {
                    getMeetingRooms();
                }
                break;
        }
    }

    private void initView() {
        meetingApply = new MeetingApply();
//        getMeetingTypes();
        setHeaderTitle(getStringFromResource(R.string.meeting_apply));
        timePickerView = new TimePickerView(getContext(), TimePickerView.Type.ALL);

        Date date = new Date();
        String dateString = dateFormat.format(date);
        timePickerView.setTime(date);
//            etStartTime.setText(dateString);
//            etEndTime.setText(dateString);
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String format = dateFormat.format(date);
                if (viewId == R.id.et_start_time) {
                    etStartTime.setText(format);
                } else if (viewId == R.id.et_end_time) {
                    etEndTime.setText(format);
                }
            }
        });
        check();
    }

    private void getMeetingRooms() {
        StringBuilder sb = new StringBuilder(Urls.MEETING_ROOM);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    meetingRooms.addAll(GsonUtil.fromJsonList(o, MeetingRoom.class));
                    getContext().pushFragmentToBackStack(MeetingRoomSelectFragment.class, meetingRooms);
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }


    private void apply() {
        StringBuilder sb = new StringBuilder(Urls.MEETIG_APPLY);

        String data = GsonUtil.objectToJson(meetingApply);
        try {
            data = URLEncoder.encode(data, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UrlUtils.getInstance(sb).praseToUrl("data", data)
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
                    ToastUtil.showShortMessage(getContext(), getStringFromResource(R.string.meeting_apply_success));
                    getContext().popTopFragment(null);
                }
            }
        };

        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void check() {
        StringBuilder sb = new StringBuilder(Urls.CHECK_NOT_COMMENT);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        UrlUtils.getInstance(sb).praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("type", "1")//会议未评价
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
                    if (o.equals("true")){
                        final CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                        builder.setTitle("消息提示");
                        builder.setMessage("你还有未评价的会议工单，请完成评价后，再提交申请！");
                        builder.setPositiveButton("去评价", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getContext().pushFragmentToBackStack(PersonalMeetingFragment.class, null);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getContext().popTopFragment(null);
                            }
                        });
                        builder.create().show();
                    }

                    }

            }
        };
        callback.setDialogHide();
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
