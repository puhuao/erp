package com.managesystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.WorkListsActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.zxing.QRResourceSendEvent;
import com.wksc.framwork.zxing.qrcodeModel.QRChecInModel;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.activity.ZxingCaptureActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.SignInOrUpEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @Bind(R.id.ll_work_list)
    LinearLayout workList;
    private IConfig config;
    private String roleName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_secretary, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        intView();
        return container;
    }

    private void intView() {
        setHeaderTitle(getStringFromResource(R.string.secretary));
        enableDefaultBack(false);
        roleName = config.getString("roleName","");
        if (!StringUtils.isBlank(roleName)&&roleName.equals(getStringFromResource(R.string.role_name_fix))){
            workList.setVisibility(View.VISIBLE);
        }else{
            workList.setVisibility(View.GONE);
        }

    }
    @OnClick({R.id.layout_scan,R.id.ll_work_list})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_scan:
                startActivity(ZxingCaptureActivity.class);
                break;
            case R.id.ll_work_list:
                startActivity(WorkListsActivity.class);
                break;
        }
    }

    @Subscribe
    public void onEvent(QRResourceSendEvent event){

    }
    @Subscribe
    public void onEvent(final SignInOrUpEvent event){
        QRChecInModel qrChecInModel = event.qrCodeModel;

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
                    int type = Integer.valueOf(event.qrCodeModel.getType());
                    if (type == ZxingCaptureActivity.MEETING_SIGN_IN){
                        ToastUtil.showShortMessage(getContext(),"会议签到成功");
                    }else if(type == ZxingCaptureActivity.MEETING_SIGN_UP){
                        ToastUtil.showShortMessage(getContext(),"会议报名成功");
                    }
                }
            }
        };
        StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
        UrlUtils.getInstance(sb).praseToUrl("meetingId",qrChecInModel.getMeetingId())
                .praseToUrl("type",qrChecInModel.getType())
                .praseToUrl("userIds",config.getString("userId", ""))
                .removeLastWord();
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
