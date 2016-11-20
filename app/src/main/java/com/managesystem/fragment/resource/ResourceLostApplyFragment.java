package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.ResNameSelectEvent;
import com.managesystem.event.ResourceSelectEcent;
import com.managesystem.fragment.meeting.MeetingDetailFragment;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.ResourceName;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.model.ResourceType;
import com.managesystem.popupwindow.ResourceListPopupwindow;
import com.managesystem.popupwindow.ResourceTypeSelectPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 物资挂失申请单
 */
public class ResourceLostApplyFragment extends CommonFragment {
@Bind(R.id.ll_type)
    LinearLayout ll;
    @Bind(R.id.name)
    TextView name;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    ResourcePersonModel resourcePersonModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_apply, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Subscribe
    public void onEvent(ResourceSelectEcent event){
        resourcePersonModel = event.getModel();
        name.setText(resourcePersonModel.getMaterialName());

    }

    private void initView() {
        hideTitleBar();
        ll.setVisibility(View.GONE);
    }

    @OnClick({R.id.name,R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                getResource(v);
                break;
            case R.id.fab:
                apply();
                break;
        }
    }

    private void apply(){
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_APPLY);//物资申请
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        UrlUtils.getInstance(sb) .praseToUrl("type", "2") .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("remark", "1") .praseToUrl("materialIds", resourcePersonModel.getMaterialId())
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
                    ToastUtil.showShortMessage(getContext(),getStringFromResource(R.string.meeting_apply_success));
                    getContext().popTopFragment(null);
                }
            }
        };

        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void getResource(final View v){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo","1")
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("keyword","")
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
                        resourcePersonModels.addAll(GsonUtil.fromJsonList(list, ResourcePersonModel.class));
                        ResourceListPopupwindow popupwindow = new ResourceListPopupwindow(getContext(),resourcePersonModels);
                        popupwindow.showPopupwindow(v);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
