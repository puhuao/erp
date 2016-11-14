package com.managesystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.model.MeetingRoomDetail;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.activity.ZxingCaptureActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.SignInOrUpEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 */
public class SecretaryFragment extends CommonFragment {
    @Bind(R.id.layout_scan)
    LinearLayout llScan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_secretary, null);
        ButterKnife.bind(this, container);
        intView();
        return container;
    }

    private void intView() {
        setHeaderTitle(getStringFromResource(R.string.secretary));
        enableDefaultBack(false);
    }
    @OnClick({R.id.layout_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_scan:
                startActivity(ZxingCaptureActivity.class);
                break;
        }
    }
    @Subscribe
    public void onEvent(final SignInOrUpEvent event){


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
                    if (event.qrCodeModel.getType() == ZxingCaptureActivity.MEETING_SIGN_IN){
                        ToastUtil.showShortMessage(getContext(),"会议签到成功");
                    }else if(event.qrCodeModel.getType() == ZxingCaptureActivity.MEETING_SIGN_UP){
                        ToastUtil.showShortMessage(getContext(),"会议报名成功");
                    }
                }
            }
        };
        StringBuilder sb = new StringBuilder(event.qrCodeModel.getUrl());
        if (event.qrCodeModel.getType() == ZxingCaptureActivity.MEETING_SIGN_IN){
             sb.append("&userIds="+config.getString("userId", ""));
        }else if(event.qrCodeModel.getType() == ZxingCaptureActivity.MEETING_SIGN_UP){
            sb.append("&userIds="+config.getString("userId", ""));
        }
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
