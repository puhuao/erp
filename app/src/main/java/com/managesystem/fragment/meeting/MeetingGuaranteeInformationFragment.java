package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.managesystem.widegt.RatingBar;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

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
    private MeetingApplyRecord meetingApplyRecord;
    String userID;

    private float rating;
    private String comment;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_meeting_guarantee, null);
        ButterKnife.bind(this, container);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        meetingApplyRecord = getArguments().getParcelable("key");
         initView();
        return container;
    }
    private void initView() {
        hideTitleBar();
        bundeDataToView();
        switch (meetingApplyRecord.getStatus()){//0：新增 1：已派单2：已确认3：已完成4：已评价
            case 0:
                llComment.setVisibility(View.GONE);
                break;
            case 1:
                llComment.setVisibility(View.GONE);
                break;
            case 2:
                llComment.setVisibility(View.GONE);
                break;
            case 3:
                llComment.setVisibility(View.VISIBLE);
                llText.setVisibility(View.GONE);
                if (userID.equals(meetingApplyRecord.getUserId())){
                    llEdit.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    ratingBar.setClickable(true);
                    ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                        @Override
                        public void onRatingChange(float RatingCount) {
rating = RatingCount;
                        }
                    });
                }
                break;
            case 4:
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
                comment = content.getText().toString();
                if (rating == 0){
                    ToastUtil.showShortMessage(getContext(),"请选择评星等级");
                    return;
                }
                if (StringUtils.isBlank(comment)){
                    ToastUtil.showShortMessage(getContext(),"请输入评价内容");
                    return;
                }
                updateDistribute();
            }
        });
    }

    private void bundeDataToView(){
        ratingBar.setStar(3f);
        tvName.setText(meetingApplyRecord.getMeetingName());
        tvStartTime.setText(meetingApplyRecord.getStartDate());
        tvLocation.setText(meetingApplyRecord.getArea());
        tvEndTime.setText(meetingApplyRecord.getEndDate());

        switch (meetingApplyRecord.getStatus()){
            case 0:
                tvGuaranteeProgress.setText("新增");
                break;
            case 1:
                tvGuaranteeProgress.setText("已受理");
                break;
            case 2:
                tvGuaranteeProgress.setText("已确认");
                break;
            case 3:
                tvGuaranteeProgress.setText("已完成");
                break;
            case 4:
                tvGuaranteeProgress.setText("已评价");
                break;
        }
        ArrayList<Users> handleUsers = meetingApplyRecord.getHandleUsers();
        StringBuilder sb = new StringBuilder();
        if (handleUsers!=null&&handleUsers.size()>0){
            for (Users user :
                    handleUsers) {
                sb.append(user.getName()).append("、");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        tvGuaranteePerson.setText(sb);
    }


    private void updateDistribute() {//评价4
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_GUARANTEE_RATING);
        UrlUtils.getInstance(sb).praseToUrl("status", String.valueOf(4))
                .praseToUrl("rid", meetingApplyRecord.getMeetingId())
                .praseToUrl("content",String.valueOf(comment) )
                .praseToUrl("star",String.valueOf(rating) )
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
}
