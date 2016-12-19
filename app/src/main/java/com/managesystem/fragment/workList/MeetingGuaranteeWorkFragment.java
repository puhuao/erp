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
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.Users;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.managesystem.widegt.RatingBar;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 会议保障工单
 */
public class MeetingGuaranteeWorkFragment extends CommonFragment {
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
    @Bind(R.id.ll_comments)
    View llComment;
    @Bind(R.id.ll_text)
    View llText;
    @Bind(R.id.ll_edit)
    View llEdit;
    @Bind(R.id.fab)
    Button fab;
    @Bind(R.id.edit_text)
    EditText content;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.responsible_name)
    TextView responsibleName;
    @Bind(R.id.responsible_phone)
    TextView responsiblePhoneNumber;
    @Bind(R.id.work_list_type)
    TextView workListType;
    @Bind(R.id.work_list_apply_department)
    TextView applyDepartment;
    @Bind(R.id.work_list_apply)
    TextView applyPerson;
    private MeetingApplyRecord meetingApplyRecord;
    String userID;

    private int rating;
    private String comment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_guarantee_work, null);
        ButterKnife.bind(this, container);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        meetingApplyRecord = (MeetingApplyRecord) getmDataIn();
        setHeaderTitle("会议工单详情");
        initView();
        return container;
    }

    private void initView() {

        bundeDataToView();
        switch (meetingApplyRecord.getStatus()) {//0：新增 1：已派单2：已确认3：已完成4：已评价
            case 0:
                tvGuaranteeProgress.setText("派单中");
                llComment.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                responsibleName.setText("暂无");
                tvGuaranteePerson.setText("暂无");
                responsiblePhoneNumber.setText("暂无");
                break;
            case 1:
                tvGuaranteeProgress.setText("已派单");
                llComment.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                responsiblePhoneNumber.setText(meetingApplyRecord.getCphone());
                CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                if (meetingApplyRecord.getResponsibleUserId().equals(userID)) {

                    builder.setMessage("收到新工单请确认");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            IConfig config = BaseApplication.getInstance().getCurrentConfig();
                            updateDistribute(config.getString("userId", ""), "2", null, null);
                        }
                    });
                    builder.setCanceldOnOutTouch(false);
                    builder.create().show();
                }else{
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
            case 2:
                tvGuaranteeProgress.setText("处理中");
                llComment.setVisibility(View.GONE);
                if (meetingApplyRecord.getHandleUsers() != null)
                    if (meetingApplyRecord.getResponsibleUserId().equals(userID)) {
                        fab.setVisibility(View.VISIBLE);
                        fab.setText("完成");
                    }
                responsiblePhoneNumber.setText(meetingApplyRecord.getCphone());
                break;
            case 3:
                tvGuaranteeProgress.setText("未评价");
                llComment.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                responsiblePhoneNumber.setText(meetingApplyRecord.getCphone());
                break;
            case 4:
                ratingBar.setStar(meetingApplyRecord.getStar());
                tvComment.setText(meetingApplyRecord.getContent());
                tvGuaranteeProgress.setText("已完成");
                llComment.setVisibility(View.VISIBLE);
                llText.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                ratingBar.setClickable(false);
                responsiblePhoneNumber.setText(meetingApplyRecord.getResponsibleUserPhone());
                break;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab.getText().toString().equals("确认")){
                    IConfig config = BaseApplication.getInstance().getCurrentConfig();
                    updateDistribute(config.getString("userId", ""), "2", null, null);
                }else{
                    IConfig config = BaseApplication.getInstance().getCurrentConfig();
                    updateDistribute(config.getString("userId", ""), "3", null, null);
                }
            }
        });
    }

    private void bundeDataToView() {
        workListType.setText("会议");
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getCtime());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());
        applyDepartment.setText(meetingApplyRecord.getDepartmentName());
        applyPerson.setText(meetingApplyRecord.getName());
        ArrayList<Users> handleUsers = meetingApplyRecord.getHandleUsers();

        StringBuilder sb = new StringBuilder();
        if (handleUsers != null && handleUsers.size() > 0) {
            for (Users user :
                    handleUsers) {
                sb.append(user.getName()).append("、");
                if (meetingApplyRecord.getResponsibleUserId().equals(user.getUserId())) {
                    responsibleName.setText(user.getName());
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        tvGuaranteePerson.setText(sb);
    }


    private void updateDistribute(String userId, String status, String comments, String rating) {//评价4完成3

        StringBuilder sb = new StringBuilder(Urls.MEETING_GUARANTEE_RATING);
        UrlUtils.getInstance(sb).praseToUrl("status", status)
                .praseToUrl("userId", userId)
                .praseToUrl("rid", meetingApplyRecord.getMeetingId())
                .praseToUrl("content", comments)
                .praseToUrl("star", rating)
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
                    if (fab.getText().toString().equals("确认")){

                        ToastUtil.showShortMessage(getContext(), "会议工单确认成功");
                        fab.setVisibility(View.GONE);
                    }else{
                        ToastUtil.showShortMessage(getContext(), "会议工单完成");
                        fab.setVisibility(View.GONE);
                    }
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
