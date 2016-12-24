package com.managesystem.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.widget.CircleImageView;
import com.managesystem.R;
import com.managesystem.activity.ExpectingActivity;
import com.managesystem.activity.MainTainListActivity;
import com.managesystem.activity.MyMeetingNoticeActivity;
import com.managesystem.activity.MyWalletActivity;
import com.managesystem.activity.PPSActivity;
import com.managesystem.activity.PersonalInfoActivity;
import com.managesystem.activity.SettingActivity;
import com.managesystem.activity.WorkListsActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnPhoneStateChangeEvent;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
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
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.department_name)
    TextView departmentName;
    @Bind(R.id.station_name)
    TextView stationName;
    @Bind(R.id.layout_scan)
    LinearLayout llScan;
    @Bind(R.id.ll_work_list)
    LinearLayout workList;
    @Bind(R.id.ll_setting)
    LinearLayout llSetting;
    @Bind(R.id.phone)
    TextView phoneNumber;
    @Bind(R.id.header)
    CircleImageView header;
@Bind(R.id.sign)
TextView tvSign;
    private IConfig config;
    private String roleName;
    private String userId;
    private String sign;

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
        userId = config.getString("userId", "");
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

        bindPhone();

        userName.setText(config.getString("name", ""));
        departmentName.setText(config.getString("department", ""));
//        tvSign.setText(StringUtils.isBlank(config.getString("sign", "")) ? "未设置" :config.getString("sign", ""));
        stationName.setText(config.getString("stationName", ""));
    }

    private void bindPhone() {
        String phone = config.getString("phone","");
        tvSign.setText(StringUtils.isBlank(config.getString("sign", "")) ? "未设置" :config.getString("sign", ""));
        if (!config.getBoolean("ispublish",false)){
            StringBuilder sb  =new StringBuilder();
            if (!StringUtils.isBlank(phone)&& phone.length() > 6){
                for (int i = 0; i < phone.length(); i++) {
                    char c = phone.charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
            }
            phoneNumber.setText(sb.toString());
        }else{
            phoneNumber.setText(phone);
        }
        Glide.with(getContext()).load(config.getString("headerIcon", ""))
                .asBitmap().centerCrop().
                into(new BitmapImageViewTarget(header) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        header.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @OnClick({R.id.layout_scan,R.id.ll_work_list,R.id.ll_maintain_list,R.id.ll_pps_list
    ,R.id.ll_setting,R.id.ll_wallet,R.id.ll_meeting_notice,R.id.ll_personal_info,R.id.ll_door
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_door:
                startActivity(ExpectingActivity.class);
                break;
            case R.id.ll_personal_info:
                startActivity(PersonalInfoActivity.class);
                break;
            case R.id.ll_meeting_notice:
                startActivity(MyMeetingNoticeActivity.class);
                break;
            case R.id.ll_wallet:
                startActivity(MyWalletActivity.class);
                break;
            case R.id.ll_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.layout_scan:
                startActivity(ZxingCaptureActivity.class);
                break;
            case R.id.ll_work_list:
                startActivity(WorkListsActivity.class);
                break;
            case R.id.ll_maintain_list:
                startActivity(MainTainListActivity.class);
                break;
            case R.id.ll_pps_list:
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
                startActivity(PPSActivity.class,bundle);
                break;
        }
    }

    @Subscribe
    public void onEvent(final QRResourceSendEvent event){
        String url = Urls.RESOURCE_SEND_TRANSFER+event.qRresourceSend.getPStr();

        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    if (event.type == 2)
                        ToastUtil.showShortMessage(getContext(),"物资发放成功");
                    else
                        ToastUtil.showShortMessage(getContext(),"物资交接成功");
                }
            }
        };
        StringBuilder sb = new StringBuilder(url);
        sb.append("&");
        UrlUtils.getInstance(sb)
                .praseToUrl("toUserId",userId)
                .removeLastWord();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
    @Subscribe
    public void onEvent(final SignInOrUpEvent event){
        QRChecInModel qrChecInModel = event.qrCodeModel;
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
                .praseToUrl("userIds",userId)
                .removeLastWord();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Subscribe
    public void onEvent(OnPhoneStateChangeEvent event){
        bindPhone();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void modify() {
        StringBuilder sb = new StringBuilder(Urls.SAVE_USER);
        String s = UrlUtils.getInstance(sb)
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("sign", sign).praseToUrl("type", "2")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "修改成功");
                    config.setString("sign", o.getSign());
                    stationName.setText(StringUtils.isBlank(config.getString("sign", "")) ? "未设置" : config.getString("sign", ""));
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }

}
