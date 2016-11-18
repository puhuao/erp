package com.managesystem.fragment.workList;

import android.content.DialogInterface;
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
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.Users;
import com.managesystem.model.WorkList;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
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
 * 工单详情
 */
public class WorkListDetailFragment extends CommonFragment {
    @Bind(R.id.work_list_type)
    TextView workListType;
    @Bind(R.id.work_list_apply)
    TextView workListApply;
    @Bind(R.id.work_list_apply_department)
    TextView department;
    @Bind(R.id.meeting_location)
    TextView location;
    @Bind(R.id.meeting_time)
    TextView time;
    @Bind(R.id.work_list_status)
    TextView status;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.fab)
    Button submit;


    private WorkList workList;
    private MeetingSelectCondition meetingSelectCondition;
    MeetingApplyRecord meetingApplyRecord;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_work_list_detail, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        workList = (WorkList) getmDataIn();
        setHeaderTitle("工单详情");
        getMeetings();
    }

    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                break;
        }
    }


    private void bundeDataToView() {
        workListType.setText(workList.getServicetypeName());
        if (meetingApplyRecord.getApplyUsers() != null) {

            StringBuilder users = new StringBuilder();
            for (Users record :
                    meetingApplyRecord.getApplyUsers()) {
                users.append(record.getName()).append("、");
            }
            if (users.length() > 0) {
                users.deleteCharAt(users.length() - 1);
            }
            workListApply.setText(users);
        }
        department.setText(meetingApplyRecord.getDepartmentName());
        location.setText(meetingApplyRecord.getArea());
        time.setText(meetingApplyRecord.getCtime());
        String statu = null;
        switch (meetingApplyRecord.getStatus()) {
            case MeetingApplyRecord.STATUS_ADD:
                statu = getStringFromResource(R.string.STATUS_ADD);
                break;
            case MeetingApplyRecord.STATUS_DISPATCH:
                statu = getStringFromResource(R.string.STATUS_DISPATCH);
                break;
            case MeetingApplyRecord.STATUS_CONFIRM:
                statu = getStringFromResource(R.string.STATUS_CONFIRM);
                break;
            case MeetingApplyRecord.STATUS_FINISH:
                statu = getStringFromResource(R.string.STATUS_FINISH);
                break;
            case MeetingApplyRecord.STATUS_COMMENT:
                statu = getStringFromResource(R.string.STATUS_COMMENT);
                break;
        }
        status.setText(statu);
        description.setText(meetingApplyRecord.getInfor());
        if (meetingApplyRecord.getStatus() == MeetingApplyRecord.STATUS_DISPATCH) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
            builder.setMessage("请确认工单");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    updateDistribute();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private void updateDistribute() {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_GUARANTEE_RATING);
        UrlUtils.getInstance(sb).praseToUrl("status", "2")
                .praseToUrl("rid", workList.getRid())
                .praseToUrl("userId", config.getString("userId", ""))
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
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }


    private void getMeetings() {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition = new MeetingSelectCondition();
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", meetingSelectCondition.getPageNo())
                .praseToUrl("userId", meetingSelectCondition.getUserId())
                .praseToUrl("pageSize", meetingSelectCondition.getPageSize())
                .praseToUrl("meetingName", meetingSelectCondition.getMeetingName())
                .praseToUrl("date", meetingSelectCondition.getDate())
                .praseToUrl("isQueryDetail", "1")
                .praseToUrl("meetingId", workList.getRid())
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
                        ArrayList<MeetingApplyRecord> applyRecords = new ArrayList<>();
                        applyRecords.addAll(GsonUtil.fromJsonList(list, MeetingApplyRecord.class));
                        meetingApplyRecord = applyRecords.get(0);
                        bundeDataToView();

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
}
