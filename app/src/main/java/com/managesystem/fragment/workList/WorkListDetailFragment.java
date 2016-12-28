package com.managesystem.fragment.workList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.WorkListFinishEvent;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.Users;
import com.managesystem.model.WorkList;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

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
    @Bind(R.id.lable)
    TextView lable;
    @Bind(R.id.maintain_name)
    TextView maintainName;
    @Bind(R.id.responsible_name)
    TextView responsibleName;
    @Bind(R.id.ll_result)
    View llResult;
    @Bind(R.id.result)
    EditText result;
    @Bind(R.id.ll_result_text)
    View llResultText;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.work_list_priority)
    TextView priority;
    @Bind(R.id.work_list_equipment_type)
    TextView equipmentType;
    @Bind(R.id.ll_equipment_type)
    LinearLayout llEquipmentType;

    private WorkList workList;
    private MeetingSelectCondition meetingSelectCondition;
    private Boolean isImportant = false;
    IConfig config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_work_list_detail, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
        return container;
    }

    private void initView() {
        workList = (WorkList) getmDataIn();
        if (!StringUtils.isBlank(workList.getImportant())){
            priority.setText(workList.getImportant());
        }
        if (workList.getServicetypeName().equals("设备")){
//            llEquipmentType.setVisibility(View.VISIBLE);
            equipmentType.setText(workList.getMaterialNames());
        }
        setHeaderTitle("工单详情");
        bundeDataToView();
    }

    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                updateDistribute(3);
                break;
        }
    }


    private void bundeDataToView() {
        if (!StringUtils.isBlank(workList.getServicetypeName())) {
            String materialNames = workList.getMaterialNames();
            if (StringUtils.isBlank(materialNames)){

                workListType.setText(workList.getServicetypeName());
            }else{
                workListType.setText(workList.getServicetypeName()+" "+workList.getMaterialNames());
            }
            lable.setText("运维人员");
        }
        StringBuilder sb = new StringBuilder();

        if (workList.getHandleUsers() != null) {
            for (Users u :
                    workList.getHandleUsers()) {

                sb.append(u.getName()).append("、");
                if (workList.getResponsibleUserId().equals(u.getUserId())) {
                    responsibleName.setText(u.getName());
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        maintainName.setText(sb);
        if (workList.getName() != null) {

            workListApply.setText(workList.getName());
        }
        department.setText(workList.getDepartmentName());
        location.setText(workList.getCphone());
        time.setText(workList.getCtime());
        String statu = null;
        switch (workList.getStatus()) {
            case MeetingApplyRecord.STATUS_ADD:
                statu = "派单中";
                llResult.setVisibility(View.GONE);
                llResultText.setVisibility(View.GONE);
                break;
            case MeetingApplyRecord.STATUS_DISPATCH:
                statu = getStringFromResource(R.string.STATUS_DISPATCH);
                llResult.setVisibility(View.GONE);
                llResultText.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                if (config.getString("userId", "").equals(workList.getResponsibleUserId())){
                    builder.setMessage("收到新工单请确认");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            updateDistribute(2);
                        }
                    });
                    builder.setCanceldOnOutTouch(false);
                    builder.create().show();
                }else {
                    builder.setMessage("请提醒责任人确认工单");
                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCanceldOnOutTouch(true);
                    builder.create().show();
                }

                break;
            case MeetingApplyRecord.STATUS_CONFIRM:
                statu = "处理中";
                llResult.setVisibility(View.VISIBLE);
                llResultText.setVisibility(View.GONE);
                if (workList.getResponsibleUserId().equals(config.getString("userId", ""))) {
                    submit.setVisibility(View.VISIBLE);
                }else{
                    submit.setVisibility(View.GONE);
                }
                break;
            case MeetingApplyRecord.STATUS_FINISH:
                statu = "未评价";
                llResult.setVisibility(View.GONE);
                llResultText.setVisibility(View.VISIBLE);
                tvResult.setText(workList.getRemark());
                submit.setVisibility(View.GONE);
                break;
            case MeetingApplyRecord.STATUS_COMMENT:
                statu = "已完成";
                llResult.setVisibility(View.GONE);
                llResultText.setVisibility(View.VISIBLE);
                tvResult.setText(workList.getHandlerInfo());
                submit.setVisibility(View.GONE);
                break;
        }
        status.setText(statu);
        description.setText(workList.getInfor());
    }

    private void updateDistribute(final int sstatus) {

        StringBuilder sb = new StringBuilder(Urls.MEETING_GUARANTEE_RATING);
        UrlUtils.getInstance(sb).praseToUrl("status", String.valueOf(sstatus))
                .praseToUrl("rid", workList.getOrderId())
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("remark",result.getText().toString())
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
                    getContext().finish();
                    EventBus.getDefault().post(new WorkListFinishEvent());
                    if (sstatus == 2) {
                        ToastUtil.showShortMessage(getContext(), "确认工单成功");
                        status.setText("处理中");
//                        fab.setVisibility(View.GONE);
                        submit.setText("完成");
                        workList.setStatus(2);
                        llResult.setVisibility(View.VISIBLE);
                        llResultText.setVisibility(View.GONE);
                        if (workList.getResponsibleUserId().equals(config.getString("userId", ""))) {
                            submit.setVisibility(View.VISIBLE);
                        }else{
                            submit.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtil.showShortMessage(getContext(), "完成工单成功");
                    }

                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
