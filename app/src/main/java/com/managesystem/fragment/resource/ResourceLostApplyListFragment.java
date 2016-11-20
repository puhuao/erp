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
import android.widget.ListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.ResourceApplyAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.meeting.MeetingDetailFragment;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.ResourceApplyLost;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 物资挂失申请记录
 */
public class ResourceLostApplyListFragment extends CommonFragment {
    @Bind(R.id.list_view)
    ListView listView;
    View empty;
    ResourceApplyAdapter adapter;
    ArrayList<ResourceApplyLost> resourceApplyLosts = new ArrayList<>();
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        empty = inflater.inflate(R.layout.empty_view, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
       hideTitleBar();
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        adapter = new ResourceApplyAdapter(getContext());
        adapter.setList(resourceApplyLosts);
        listView.setAdapter(adapter);
        getRecords();
    }

    private void getRecords(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_APPLY_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo","1")
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("type","2")
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
                        resourceApplyLosts.addAll(GsonUtil.fromJsonList(list, ResourceApplyLost.class));
                        adapter.notifyDataSetChanged();
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

}
