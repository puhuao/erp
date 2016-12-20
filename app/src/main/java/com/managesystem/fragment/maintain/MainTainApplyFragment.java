package com.managesystem.fragment.maintain;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.MainTainListActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.GoToComment;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.model.MeetingType;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 运维服务申请
 */
public class MainTainApplyFragment extends CommonFragment {
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.ll_equipment_name)
    View llEquipment;
    @Bind(R.id.equipment_name)
    TextView tvEquipmentName;
    @Bind(R.id.remark)
    EditText remark;
    private MeetingType meetingType;
    private int equipmentType = 0;//0表示其他1表示设备2表示电话
    private String equipmentNames;
    IConfig config;

    private int flag = 0;//1从运维服务进来，0从我的物资保修进来
Bundle bundle;
    String name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_maitain_apply, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        bundle = (Bundle) getmDataIn();

        initView();
        return container;
    }

    @Subscribe
    public void onEvent(MeetingTypeSelectEvent event) {
        meetingType = event.getMeetingType();
        if (event.getEquipmentName() != null) {
            equipmentNames = event.getEquipmentName();
            llEquipment.setVisibility(View.VISIBLE);
            tvEquipmentName.setText(event.getEquipmentName());
            type.setText(meetingType.getServicetypeName());
            return;
        }
        if (meetingType.getServicetypeName().equals("电话")) {
            equipmentType = 2;
            remark.setText(config.getString("cphone", ""));

        }
        llEquipment.setVisibility(View.GONE);
        tvEquipmentName.setText("");
        type.setText(meetingType.getServicetypeName());
    }

    private void initView() {
        setHeaderTitle("运维服务申请");
        flag= bundle.getInt("type");
        if (flag == 0){
            type.setCompoundDrawables(null,null,null,null);
            tvEquipmentName.setCompoundDrawables(null,null,null,null);
            name = bundle.getString("string");
            getMeetingTypes(name);
        }else{
            llEquipment.setVisibility(View.GONE);
        }
        check();
    }

    @OnClick({R.id.type, R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                apply();
                break;
            case R.id.type:
                if (flag==1)
                getContext().pushFragmentToBackStack(MaintainResourceTypeFragment.class, null);
                break;

        }
    }

    private void apply() {
        if (meetingType != null) {
            StringBuilder sb = new StringBuilder(Urls.MAINTAIN_APPLY);//运维申请

            UrlUtils.getInstance(sb).praseToUrl("userId", config.getString("userId", ""))
                    .praseToUrl("infor", remark.getText().toString()).praseToUrl("servicetypeId", meetingType.getServicetypeId())
                    .praseToUrl("materialNames", equipmentNames)
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
                        ToastUtil.showShortMessage(getContext(), "运维服务申请成功");
                        getContext().finish();
                    }
                }
            };
            OkHttpUtils.post(sb.toString())//
                    .tag(this)//
                    .execute(callback);
        } else {
            ToastUtil.showShortMessage(getContext(), "请选择设备");
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getMeetingTypes(final String name){
        StringBuilder sb = new StringBuilder(Urls.METTING_TYPES);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    List<MeetingType> list = GsonUtil.fromJsonList(o, MeetingType.class);
                    for (MeetingType t :
                            list) {
                        if (t.getServicetypeName().equals("设备")){
                            EventBus.getDefault().post(new MeetingTypeSelectEvent(t,name) );
                        }
                    }
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void check() {
        StringBuilder sb = new StringBuilder(Urls.CHECK_NOT_COMMENT);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        UrlUtils.getInstance(sb).praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("type", "2")//工单未评价
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
                    if (o.equals("true")) {
                        final CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                        builder.setTitle("消息提示");
                        builder.setMessage("你还有未评价的工单，请完成评价后，再提交申请！");
                        builder.setPositiveButton("去评价", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                startActivity(MainTainListActivity.class);
                                getContext().pushFragmentToBackStack(MaintainApplyRecordFragment.class,null);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getContext().finish();
                            }
                        });
                        builder.create().show();
                    }
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }


}
