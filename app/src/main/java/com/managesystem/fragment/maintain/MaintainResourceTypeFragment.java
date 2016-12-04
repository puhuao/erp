package com.managesystem.fragment.maintain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.PopMeetingTypeAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.model.MeetingType;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.widget.LoadMoreListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 运维申请时服务类型页面
 */
public class MaintainResourceTypeFragment extends CommonFragment {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list_view)
    LoadMoreListView listView;
    View empty;
    PopMeetingTypeAdapter adapter;
    private ArrayList<MeetingType> meetingTypes = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        empty = inflater.inflate(R.layout.empty_view, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("服务类型");
        adapter = new PopMeetingTypeAdapter(getContext());
        listView.setAdapter(adapter);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        adapter.setList(meetingTypes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=meetingTypes.size())
                if (meetingTypes.get(position).getServicetypeName().equals("设备")){

                    getContext().pushFragmentToBackStack(MaintainResourcePersonalFragment.class,
                            meetingTypes.get(position));
                }else{
                    String name = meetingTypes.get(position).getServicetypeName();
                    EventBus.getDefault().post(new MeetingTypeSelectEvent(meetingTypes.get(position),null));
                    getContext().popTopFragment(null);
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMeetingTypes();
            }
        });
        getMeetingTypes();
    }

    private void getMeetingTypes(){
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
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(true);
                    }
                    listView.onLoadMoreComplete();
                    listView.setCanLoadMore(false);
                    meetingTypes.clear();
                    meetingTypes.addAll(GsonUtil.fromJsonList(o, MeetingType.class));
                    adapter.notifyDataSetChanged();
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
