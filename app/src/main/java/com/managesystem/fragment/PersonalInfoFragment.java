package com.managesystem.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnDepartmentModifyedEvent;
import com.managesystem.fragment.modify.ModifyAccountFragment;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

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

    private IConfig config;
    private String name;
    private String userId;
    private String cPhone;
    private String department;
    private String sign;


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
        tvName.setText(StringUtils.isBlank(config.getString("name",""))?"无":config.getString("name",""));
        tvCPhone.setText(StringUtils.isBlank(config.getString("cphone",""))?"未设置":config.getString("cphone",""));
        tvDepartment.setText(StringUtils.isBlank(config.getString("department",""))?"未设置":config.getString("department",""));
        tvPersonalSign.setText(StringUtils.isBlank(config.getString("sign",""))?"未设置":config.getString("sign",""));
        tvAccount.setText(StringUtils.isBlank(config.getString("username",""))?"":config.getString("username",""));
        Glide.with(getContext())
                .load(config.getString("headerIcon","")).crossFade()
                .placeholder(R.drawable.ic_header_defalt)
                .error(R.drawable.ic_header_defalt)
                .thumbnail(0.1f).centerCrop()
                .into(imHeader);
    }

    @OnClick({R.id.ll_head_pic,R.id.ll_name,R.id.ll_cPhone,R.id.ll_department
    ,R.id.ll_personal_sign,R.id.ll_account_modify})
    public void onClick(final View v) {
        final CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_edit_text,null);
        builder.setContentView(view);
        switch (v.getId()) {
            case R.id.ll_account_modify:
                                getContext().pushFragmentToBackStack(ModifyAccountFragment.class,null);
                break;
            case R.id.ll_personal_sign:
                builder.setTitle("请输入签名");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sign = ((EditText) view.findViewById(R.id.edit_text)).getText().toString();
                        if (StringUtils.isBlank(sign)){
                            ToastUtil.showShortMessage(getContext(),"请输入签名");
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
                break;
            case R.id.ll_name:
                builder.setTitle("请输入姓名");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        name = ((EditText) view.findViewById(R.id.edit_text)).getText().toString();
                        if (StringUtils.isBlank(name)){
                            ToastUtil.showShortMessage(getContext(),"请输入姓名");
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
            case R.id.ll_cPhone:
                builder.setTitle("请输入座机号");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cPhone = ((EditText) view.findViewById(R.id.edit_text)).getText().toString();
                        if (StringUtils.isBlank(cPhone)){
                            ToastUtil.showShortMessage(getContext(),"请输入座机号");
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
            case R.id.ll_department:
                break;
        }
    }

    @Subscribe
    public void onEvent( OnDepartmentModifyedEvent event){
        bindView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void modify(){
        StringBuilder sb = new StringBuilder(Urls.SAVE_USER);
        String s = UrlUtils.getInstance(sb)
                .praseToUrl("userId",config.getString("userId",""))
                .praseToUrl("sign", sign).praseToUrl("type","2")
                .praseToUrl("cphone",cPhone).praseToUrl("ispublish","1")
                .praseToUrl("name",name)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"修改成功");
                    config.setString("roleName",o.getRoleName());
                    config.setString("name",o.getName());
                    config.setString("stationName",o.getStationName());
                    config.setString("department",o.getDepartmentName());
                    config.setString("cphone",o.getCphone());
                    config.setString("headerIcon",o.getHeadPic());
                    config.setBoolean("isLogin",true);
                    config.setString("sign",o.getSign());
                    bindView();
                }
            }
        };
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }



}
