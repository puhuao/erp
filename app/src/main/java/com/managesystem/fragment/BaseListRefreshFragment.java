package com.managesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.BaseListAdapter;
import com.managesystem.callBack.DialogCallback;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.widget.LoadMoreListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by puhua on 2016/11/23.
 *
 * @
 */

public abstract class BaseListRefreshFragment<T> extends CommonFragment{
    protected int pageNo = 1;
    protected int pageSize = 20;
    @Bind(R.id.swipe_refresh)
   public SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list_view)
    public LoadMoreListView listView;
    View empty;
    ArrayList<T> models ;
    BaseListAdapter<T> adapter;
    public boolean isFirstLoad = true;
    public boolean isfirstFragment = false;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startRefresh();
            pageNo = 1;
            loadMore(1);
        }
    };

    public void setData(ArrayList<T> models,BaseListAdapter<T> adapter){
        this.models = models;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,container);
        empty = inflater.inflate(R.layout.empty_view, null);
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore(pageNo);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                loadMore(1);
            }
        });
        listView.setAdapter(adapter);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
        adapter.setList(models);
        if (isfirstFragment){
            startRefresh();
            loadMore(1);
        }
        return container;
    }

    public void notifyData(String o,Class<T> clazz) {
        listView.onLoadMoreComplete();
        try {
            JSONObject jsonObject = new JSONObject(o);
            String list = jsonObject.getString("list");
            if (pageNo == 1){
                models.clear();
            }
            List<T> elements = GsonUtil.fromJsonList(list, clazz);
            if (elements != null && elements.size() > 0) {
                pageNo++;
                isFirstLoad = false;
                models.addAll(elements);
                if (elements.size() < pageSize) {
                    listView.setCanLoadMore(false);
                }
            } else {
                listView.setCanLoadMore(false);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置顶部正在加载的状态
     */
    protected void startRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    // 防止多次重复刷新
                    swipeRefreshLayout.setEnabled(false);
                }
            });
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void stopRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(true);
        }
    }

    public abstract void loadMore(int pageNo);

    protected DialogCallback callback;
    public void excute(String s, final Class<T> meetingRoomDetailClass){
        callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                stopRefresh();
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }
            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    stopRefresh();
                    notifyData(o,meetingRoomDetailClass);
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.get(s)//
                .tag(this)//
                .execute(callback);
    }
}
