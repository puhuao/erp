package com.managesystem.fragment.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.GridImageAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.event.ResNameSelectEvent;
import com.managesystem.event.ResTypeSelectEvent;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingType;
import com.managesystem.model.ResourceName;
import com.managesystem.model.ResourceType;
import com.managesystem.popupwindow.MeetingTypeSelectPopupwindow;
import com.managesystem.popupwindow.ResourceNameSelectPopupwindow;
import com.managesystem.popupwindow.ResourceTypeSelectPopupwindow;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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
    private ArrayList<MeetingType> meetingTypes = new ArrayList<>();

    private MeetingType meetingType;
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
        meetingType = event.getDepartment();
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
                    getMeetingTypes(type);
                break;

        }
    }

    private void getMeetingTypes(final View v){
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
                    meetingTypes.addAll(GsonUtil.fromJsonList(o, MeetingType.class));
                        MeetingTypeSelectPopupwindow popupwindow = new MeetingTypeSelectPopupwindow(getContext(),meetingTypes);
                        popupwindow.showPopupwindow(v);
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void apply(){
        StringBuilder sb = new StringBuilder(Urls.MAINTAIN_APPLY);//运维申请
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        UrlUtils.getInstance(sb) .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("infor", "1") .praseToUrl("servicetypeId", meetingType.getServicetypeId())
                .praseToUrl("materialNames", meetingType.getServicetypeName())
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
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
