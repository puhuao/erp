package com.managesystem.fragment.meeting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.MeetingApplyRecordAdapter;
import com.managesystem.adapter.MeetingNoticePersonAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.AddUserParam;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.Users;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 我的会议里人员列表，根据不同的值确定不同的内容
 */
public class MeetingPersonListFragment extends CommonFragment{
    @Bind(R.id.list_view)
    ListView listView ;
    @Bind(R.id.fab)
    Button fab;
    MeetingNoticePersonAdapter adapter;
    private List<Users> list;
    private ArrayList<String> users = new ArrayList<>();
    private int type;//0报名人员列表1参会人员列表2通知下一议题侯会人
    private String meetingId;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.pop_department_select, null);
        ButterKnife.bind(this, container);
        Bundle bundle = (Bundle) getmDataIn();
        list = (List<Users>) bundle.getSerializable("list");
        type = bundle.getInt("type");
        meetingId = bundle.getString("meetingId");
        initView();
        return container;
    }

    private void initView() {
        adapter = new MeetingNoticePersonAdapter(getContext());
        switch (type){
            case 0:
                setHeaderTitle("会议报名人员");
                adapter.setType(0);
                break;
            case 1:
                setHeaderTitle("实际参会人员");
                adapter.setType(1);
                break;
            case 2:
                fab.setVisibility(View.VISIBLE);
                adapter.setType(2);
                setHeaderTitle("侯会人员列表");
                break;
        }
        listView.setAdapter(adapter);
        for (Users u :
                list) {
            u.isCheck = false;
        }
        adapter.setList(list);
    }

    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                for (Users u :
                        list) {
                    if (u.isCheck){
                        users.add(u.getUserId());
                    }
                }
                notice();
                break;
        }
    }


    private void notice(){
        StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
        UrlUtils utils = UrlUtils.getInstance(sb).praseToUrl("meetingId",meetingId)
                .praseToUrl("type","0");
        for (String s :
                users) {
            utils.praseToUrl("userIds",s);
        }
        utils .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"通知用户成功");
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
