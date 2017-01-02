package com.managesystem.fragment.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.MeetingMsgDetailActivity;
import com.managesystem.adapter.MsgReadAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnMSGNoticeEvent;
import com.managesystem.event.UpdateMsgListEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.Message;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 消息未读
 */
public class MsgNotReadFragment extends BaseListRefreshFragment<Message> {
    MsgReadAdapter msgReadAdapter;
    ArrayList<Message> messages = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Subscribe
    public void onEvent(UpdateMsgListEvent event){
        pageNo=1;
        loadMore(1);
    }

    private void initView() {
        isfirstFragment = true;
        hideTitleBar();
        msgReadAdapter = new MsgReadAdapter(getContext());
        setData(messages,msgReadAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=messages.size()){

                    Intent i = new Intent(getContext(), MeetingMsgDetailActivity.class);
                    Message message = messages.get(position);
                    i.putExtra("obj",message);
                    i.putExtra("flag",1);
                    getContext().startActivity(i);
                }
            }
        });
        l = new OnDataLoadListener() {
            @Override
            public void onload(List elements) {
                if (elements!=null)
                    EventBus.getDefault().post(new OnMSGNoticeEvent(totalCount));
            }
        };
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MSG_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("status","0")
                .removeLastWord();
        excuteWithBack(sb.toString(),Message.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void markAllRead() {
        markAll();
    }


    private void markAll(){
        StringBuilder sb = new StringBuilder(Urls.MARK_ALL);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
            UrlUtils.getInstance(sb).praseToUrl("userId",config.getString("userId", ""))
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
                    EventBus.getDefault().post(new UpdateMsgListEvent());
                    EventBus.getDefault().post(new OnMSGNoticeEvent(0));
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
