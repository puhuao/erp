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
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.jpush.JPUSHModel;
import com.managesystem.model.AddUserParam;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.Message;
import com.managesystem.popupwindow.MeetingNoticeNextPersonPopupwindow;
import com.managesystem.popupwindow.MeetingSignPersonPopupwindow;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.activity.ZxingCaptureActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;

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
public class MeetingMSGDetailFragment extends CommonFragment {
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.fab)
    Button fab;
    Message message;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_msg_detail, null);
        ButterKnife.bind(this, container);
        message = (Message) getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        hideTitleBar();
        content.setText(message.content);
    }

    @OnClick({R.id.fab,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                signUp();
                break;
        }
    }

    public void signUp(){

        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                        ToastUtil.showShortMessage(getContext(),"会议报名成功");
                }
            }
        };
        StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
        UrlUtils.getInstance(sb).praseToUrl("meetingId",message.rid)
                .praseToUrl("type","1")
                .praseToUrl("userIds",config.getString("userId", ""))
                .removeLastWord();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
