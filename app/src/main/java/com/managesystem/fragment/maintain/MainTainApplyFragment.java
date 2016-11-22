package com.managesystem.fragment.maintain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.model.MeetingType;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
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
 * Created by Administrator on 2016/11/8.
 * 运维服务申请
 */
public class MainTainApplyFragment extends CommonFragment {
@Bind(R.id.type)
    TextView type;
    private MeetingType meetingType;
    private int equipmentType = 0;//0表示其他1表示设备2表示电话
    private String equipmentNames;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_maitain_apply, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }
    @Subscribe
    public void onEvent(MeetingTypeSelectEvent event){
        meetingType = event.getMeetingType();
        if (event.getEquipmentName()!=null){
            equipmentNames = event.getEquipmentName();
            type.setText(meetingType.getServicetypeName()+" "+event.getEquipmentName());
            return;
        }
        if (meetingType.getServicetypeName().equals("电话")){
            equipmentType = 2;
        }
        type.setText(meetingType.getServicetypeName());

    }
    private void initView() {
        setHeaderTitle("运维服务申请");
    }

    @OnClick({R.id.type,R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                apply();
                break;
            case R.id.type:
                getContext().pushFragmentToBackStack(MaintainResourceTypeFragment.class, null);
                break;

        }
    }
    private void apply(){
        if (meetingType!=null){
            StringBuilder sb = new StringBuilder(Urls.MAINTAIN_APPLY);//运维申请
            IConfig config = BaseApplication.getInstance().getCurrentConfig();
            UrlUtils.getInstance(sb) .praseToUrl("userId",config.getString("userId", ""))
                    .praseToUrl("infor", "1") .praseToUrl("servicetypeId", meetingType.getServicetypeId())
                    .praseToUrl("materialNames", equipmentNames)
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
                        ToastUtil.showShortMessage(getContext(),"运维服务申请成功");
                        getContext().finish();
                    }
                }
            };
            OkHttpUtils.post(sb.toString())//
                    .tag(this)//
                    .execute(callback);
        }else{
            ToastUtil.showShortMessage(getContext(),"请选择设备");
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
