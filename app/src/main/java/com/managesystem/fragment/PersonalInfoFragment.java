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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnDepartmentModifyedEvent;
import com.managesystem.event.OnPhoneStateChangeEvent;
import com.managesystem.fragment.modify.ModifyAccountFragment;
import com.managesystem.fragment.modify.ModifyAccountIsPublishFragment;
import com.managesystem.fragment.modify.ModifyPasswordFragment;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.managesystem.widegt.SwitchButton;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.widget.CircleImageView;

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
 * 个人信息
 */
public class PersonalInfoFragment extends CommonFragment {
    @Bind(R.id.ic_header)
    ImageView imHeader;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_cPhone)
    TextView tvCPhone;
    @Bind(R.id.tv_department)
    TextView tvDepartment;
    @Bind(R.id.tv_personal_sign)
    TextView tvPersonalSign;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.tv_is_phone_public)
    TextView tvIsPublicPhone;
    @Bind(R.id.ll_is_phone_public)
    View llIsPublicPhone;
    @Bind(R.id.phone_public)
    SwitchButton switchButton;

    private IConfig config;
    private String name;
    private String userId;
    private String cPhone;
    private String department;
    private String sign;
    private String ispublish;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_personal_info, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        userId = config.getString("userId", "");
        intView();
        return container;
    }

    private void intView() {
        setHeaderTitle("个人信息");
        bindView();
    }

    private void bindView() {
        switchButton.setChecked(config.getBoolean("ispublish",false)?true:false);
        tvIsPublicPhone.setText(config.getBoolean("ispublish",false)?"是":"否");
        tvState.setText(StringUtils.isBlank(config.getString("stationName", "")) ? "无" : config.getString("stationName", ""));
        tvName.setText(StringUtils.isBlank(config.getString("name", "")) ? "无" : config.getString("name", ""));
        tvCPhone.setText(StringUtils.isBlank(config.getString("cphone", "")) ? "未设置" : config.getString("cphone", ""));
        tvDepartment.setText(StringUtils.isBlank(config.getString("department", "")) ? "未设置" : config.getString("department", ""));
        tvPersonalSign.setText(StringUtils.isBlank(config.getString("sign", "")) ? "未设置" : config.getString("sign", ""));
        tvAccount.setText("");
        Glide.with(getContext()).load(config.getString("headerIcon", ""))
                .asBitmap().centerCrop().
                into(new BitmapImageViewTarget(imHeader) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imHeader.setImageDrawable(circularBitmapDrawable);
                    }
                });
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ispublish = "1";
                    modify();
                }else{
                    ispublish = "0";
                    modify();
                }
            }
        });
    }

    @OnClick({R.id.ll_head_pic, R.id.ll_name, R.id.ll_cPhone, R.id.ll_department
            , R.id.ll_personal_sign, R.id.ll_account_modify,R.id.ll_is_phone_public,R.id.ll_password_modify})
    public void onClick(final View v) {
        final CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_edit_text, null);
        builder.setContentView(view);
        switch (v.getId()) {
            case R.id.ll_password_modify:
                getContext().pushFragmentToBackStack(ModifyPasswordFragment.class, null);
                break;
            case R.id.ll_is_phone_public:
//                getContext().pushFragmentToBackStack(ModifyAccountIsPublishFragment.class, null);
                break;
            case R.id.ll_account_modify:
                getContext().pushFragmentToBackStack(ModifyAccountFragment.class, null);
                break;
            case R.id.ll_personal_sign:
                builder.setTitle("请输入签名");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        EditText editText = ((EditText) view.findViewById(R.id.edit_text));
                        editText.setMaxEms(20);
                        sign =editText .getText().toString();
                        if (StringUtils.isBlank(sign)) {
                            ToastUtil.showShortMessage(getContext(), "请输入签名");
                            return;
                        }
                        modify();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.ll_head_pic:
                getContext().pushFragmentToBackStack(PersonalHeadicUploadFragment.class, null);
                break;
            case R.id.ll_name:
//                builder.setTitle("请输入姓名");
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        name = ((EditText) view.findViewById(R.id.edit_text)).getText().toString();
//                        if (StringUtils.isBlank(name)) {
//                            ToastUtil.showShortMessage(getContext(), "请输入姓名");
//                            return;
//                        }
//                        modify();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
                break;
            case R.id.ll_cPhone:
//                builder.setTitle("请输入座机号");
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        cPhone = ((EditText) view.findViewById(R.id.edit_text)).getText().toString();
//                        if (StringUtils.isBlank(cPhone)) {
//                            ToastUtil.showShortMessage(getContext(), "请输入座机号");
//                            return;
//                        }
//                        modify();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
                break;
            case R.id.ll_department:
                break;
        }
    }

    @Subscribe
    public void onEvent(OnDepartmentModifyedEvent event) {
        bindView();
        EventBus.getDefault().post(new OnPhoneStateChangeEvent());
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
                .praseToUrl("cphone", cPhone).praseToUrl("ispublish", ispublish)
                .praseToUrl("name", name)
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
                    config.setString("roleName", o.getRoleName());
                    config.setString("name", o.getName());
                    config.setString("stationName", o.getStationName());
                    config.setString("department", o.getDepartmentName());
                    config.setString("cphone", o.getCphone());
                    config.setString("headerIcon", Urls.GETPICS+o.getHeadPic());
                    config.setBoolean("isLogin", true);
                    config.setString("sign", o.getSign());
                    if (o.getIspublish()==1){
                        config.setBoolean("ispublish",true);
                    }else{
                        config.setBoolean("ispublish",false);
                    }
                    if (!StringUtils.isBlank(ispublish))
                        EventBus.getDefault().post(new OnPhoneStateChangeEvent());
                    bindView();
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }


}
