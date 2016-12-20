package com.managesystem.fragment.meeting;

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
import com.managesystem.event.OnMeetingGuaranteeCommented;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.Users;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.RatingBar;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 保障信息
 */
public class MeetingGuaranteeInformationFragment extends CommonFragment {
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
    private MeetingApplyRecord meetingApplyRecord;
    String userID;

    private int rating;
    private String comment;
    Bundle bundle;
    private int type=0;//0会议保障详情1会议工单详情

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_guarantee, null);
        ButterKnife.bind(this, container);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        meetingApplyRecord= (MeetingApplyRecord) getmDataIn();
        if (meetingApplyRecord==null){
            meetingApplyRecord = getArguments().getParcelable("key");
            hideTitleBar();
            type = 0;
        }
        else{
            setHeaderTitle("会议保障详情");
            type = 1;

        }
        initView();
        return container;
    }

    private void initView() {

        bundeDataToView();
        switch (meetingApplyRecord.getStatus()) {//0：新增 1：已派单2：已确认3：已完成4：已评价
            case 0:
                tvGuaranteeProgress.setText("未派单");
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
                responsiblePhoneNumber.setText(meetingApplyRecord.getResponsibleUserPhone());
                break;
            case 2:
                tvGuaranteeProgress.setText("处理中");
                llComment.setVisibility(View.GONE);
//                if (type == 1){
//                    fab.setVisibility(View.VISIBLE);
//                    fab.setText("完成");
//                }
                responsiblePhoneNumber.setText(meetingApplyRecord.getResponsibleUserPhone());
                break;
            case 3:
                responsiblePhoneNumber.setText(meetingApplyRecord.getResponsibleUserPhone());
                    tvGuaranteeProgress.setText("未评价");
                    llComment.setVisibility(View.VISIBLE);
                    llText.setVisibility(View.GONE);
                    if (userID.equals(meetingApplyRecord.getUserId())) {
                        llEdit.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        ratingBar.setClickable(true);
                        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                            @Override
                            public void onRatingChange(float RatingCount) {
                                rating = (int) RatingCount;
                            }
                        });
                    }

                break;
            case 4:
                responsiblePhoneNumber.setText(meetingApplyRecord.getResponsibleUserPhone());
                ratingBar.setStar(meetingApplyRecord.getStar());
                tvComment.setText(meetingApplyRecord.getContent());
                tvGuaranteeProgress.setText("已完成");
                llComment.setVisibility(View.VISIBLE);
                llText.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                ratingBar.setClickable(false);
                break;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IConfig config = BaseApplication.getInstance().getCurrentConfig();
                    comment = content.getText().toString();
                    if (rating == 0) {
                        ToastUtil.showShortMessage(getContext(), "请选择评星等级");
                        return;
                    }
                    if (StringUtils.isBlank(comment)) {
                        ToastUtil.showShortMessage(getContext(), "请输入评价内容");
                        return;
                    }

                    updateDistribute(null,"4",comment,String.valueOf(rating));
            }
        });
    }

    private void bundeDataToView() {
//        ratingBar.setStar(3f);
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());
        ArrayList<Users> handleUsers = meetingApplyRecord.getHandleUsers();
        StringBuilder sb = new StringBuilder();
        if (handleUsers != null && handleUsers.size() > 0) {
            for (Users user :
                    handleUsers) {
                sb.append(user.getName()).append("、");
                if (meetingApplyRecord.getResponsibleUserId().equals(user.getUserId())){
                    responsibleName.setText(user.getName());
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        tvGuaranteePerson.setText(sb);
    }


    private void updateDistribute(String userId,String status,String comments,String rating) {//评价4完成3

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
                        ToastUtil.showShortMessage(getContext(), "会议保障评价成功");
                        ratingBar.setClickable(false);
                        llEdit.setVisibility(View.GONE);
                        llText.setVisibility(View.VISIBLE);
                        tvComment.setText(comment);
                    EventBus.getDefault().post(new OnMeetingGuaranteeCommented());
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
