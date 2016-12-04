package com.managesystem.fragment.maintain;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.AddUserParam;
import com.managesystem.model.Maintain;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.Users;
import com.managesystem.popupwindow.MeetingNoticeNextPersonPopupwindow;
import com.managesystem.popupwindow.MeetingSignPersonPopupwindow;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.RatingBar;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
public class MaintainDetailFragment extends CommonFragment {
    @Bind(R.id.guarantee_progress)
    TextView guaranteeProgress;
    @Bind(R.id.name)
    TextView tvName;
    @Bind(R.id.start_time)
    TextView tvStartTime;
    @Bind(R.id.location)
    TextView tvLocation;
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
    @Bind(R.id.ll_reason)
            View llReason;
    @Bind(R.id.problem)
            TextView handlerInfo;
    @Bind(R.id.content)
            TextView tvComment;
    @Bind(R.id.responsible_name)
    TextView responsibleName;
    @Bind(R.id.responsible_phone)
    TextView responsiblePhoneNumber;
    String userID;
    private String oderId;

    private int rating;
    private String comment;
    Maintain maintain;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_maintain_detail, null);
        ButterKnife.bind(this, container);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        oderId = (String) getmDataIn();
        getMaintains(oderId);

        return container;
    }

    private void initView() {
        StringBuilder sb = new StringBuilder();
        if (maintain.getHandleUsers()!=null){
            for (Users u :
                    maintain.getHandleUsers()) {
                sb.append(u.getName()+",");
            }
            if (sb.length()>0)
                sb.deleteCharAt(sb.length()-1);
        }
        handlerInfo.setText(maintain.getHandlerInfo());
        tvName.setText(maintain.getServicetypeName());//服务名称
        tvStartTime.setText(maintain.getCtime());//申请实际那
        responsibleName.setText(maintain.getResponsibleUserName()==null?"暂无":maintain.getResponsibleUserName());
        responsiblePhoneNumber.setText(maintain.getResponsibleUserPhone()==null?"暂无":maintain.getResponsibleUserPhone());
        tvLocation.setText(maintain.getHandlerInfo()==null?"暂无":maintain.getHandlerInfo());//维修详情
        tvGuaranteePerson.setText(maintain.getResponsibleUserId()==null?"暂无":maintain.getResponsibleUserId());
        switch (maintain.getStatus()){//0：新增 1：已派单2：已确认3：已完成4：已评价
            case 0:
                setHeaderTitle("新增");
                guaranteeProgress.setText("新增");
                llComment.setVisibility(View.GONE);
                tvGuaranteePerson.setText("暂无");
                break;
            case 1:

                tvGuaranteePerson.setText(sb.toString());
                setHeaderTitle("已派单");
                guaranteeProgress.setText("已派单");
                llComment.setVisibility(View.GONE);
                break;
            case 2:
                tvGuaranteePerson.setText(sb.toString());
                setHeaderTitle("已确认");
                guaranteeProgress.setText("已确认");
                llComment.setVisibility(View.GONE);
                break;
            case 3:
                tvGuaranteePerson.setText(sb.toString());
                setHeaderTitle("已完成");
                guaranteeProgress.setText("已完成");
                llComment.setVisibility(View.VISIBLE);
                llText.setVisibility(View.GONE);
                llReason.setVisibility(View.VISIBLE);

                if (userID.equals(maintain.getUserId())){
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
                tvGuaranteePerson.setText(sb.toString());
                setHeaderTitle("已评价");
                tvComment.setText(maintain.getContent());
                guaranteeProgress.setText("已评价");
                llComment.setVisibility(View.VISIBLE);
                llText.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                llReason.setVisibility(View.VISIBLE);
                ratingBar.setClickable(false);
                ratingBar.setStar(maintain.getStar());
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



    private void updateDistribute() {//评价4
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_GUARANTEE_RATING);
        UrlUtils.getInstance(sb).praseToUrl("status", String.valueOf(4))
                .praseToUrl("rid", maintain.getOrderId())
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
                    ToastUtil.showShortMessage(getContext(), "评价成功");
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void getMaintains(String orderId){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MAINTAIN_LIST_DETAIL);
        UrlUtils.getInstance(sb).praseToUrl("pageNo","1")
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("isQueryDetail","1")
                .praseToUrl("orderId",orderId)
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
                        List<Maintain> maintains =  GsonUtil.fromJsonList(list, Maintain.class);
                        maintain = maintains.get(0);
                        initView();
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
