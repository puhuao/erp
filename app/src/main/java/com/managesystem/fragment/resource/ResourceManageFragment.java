package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.GoodNewsActivity;
import com.managesystem.activity.MeetingManageActivity;
import com.managesystem.activity.RecycleZxingCaptureActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.goodnews.GoodNewsSingInFragment;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.activity.ZxingCaptureActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.QRResourceRecycleEvent;
import com.wksc.framwork.zxing.QRResourceSendEvent;

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
 *
 */
public class ResourceManageFragment extends CommonFragment {
@Bind(R.id.rl_resource_yard)
View yard;
    @Bind(R.id.rl_resource_recycle)
    View recycle;
    private IConfig config;
    private String roleName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_manage, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.resource_manage));
        roleName = config.getString("roleName","");
        if (!StringUtils.isBlank(roleName)&&
                roleName.contains(getStringFromResource(R.string.role_name_resource_manage))){
            yard.setVisibility(View.VISIBLE);
            recycle.setVisibility(View.VISIBLE);
        }else{
            yard.setVisibility(View.INVISIBLE);
            recycle.setVisibility(View.GONE);
        }
    }


    @Subscribe
    public void onEvent(final QRResourceRecycleEvent event){
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
                    ToastUtil.showShortMessage(getContext(),"物资回收成功");
                }
            }
        };
        StringBuilder sb = new StringBuilder(url);
        sb.append("&");
        UrlUtils.getInstance(sb)
                .praseToUrl("toUserId",config.getString("userId",""))
                .removeLastWord();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @OnClick({R.id.rl_resource_person,R.id.rl_resource_apply,R.id.rl_resource_lost,R.id.rl_resource_yard,R.id.rl_resource_recycle})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_resource_person:
                getContext().pushFragmentToBackStack(ResourcePersonalFragment.class,null);
                break;
            case R.id.rl_resource_apply:
                getContext().pushFragmentToBackStack(PersonalResourceApplyFragment.class,null);
                break;
            case R.id.rl_resource_lost:
                getContext().pushFragmentToBackStack(PersonalResourceLostFragment.class,null);
                break;
            case R.id.rl_resource_yard:
                getContext().pushFragmentToBackStack(ResourceYardFragment.class,null);
                break;
            case R.id.rl_resource_recycle:
                startActivity(RecycleZxingCaptureActivity.class);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
