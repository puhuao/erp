package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.MeetingAttendRecordAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.MeetingAttendRecord;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.NestedListView;
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
 * Created by Administrator on 2016/11/5.
 * 参会记录
 */
public class MeetingAttendRecordFragment extends CommonFragment {
    @Bind(R.id.list_view)
    ListView listView;
    MeetingAttendRecordAdapter adapter;
    ArrayList<MeetingAttendRecord> records = new ArrayList<>();
    View empty;
    private MeetingSelectCondition meetingSelectCondition;

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
        meetingSelectCondition = new MeetingSelectCondition();
        adapter = new MeetingAttendRecordAdapter(getContext());
        listView.setAdapter(adapter);
        ((ViewGroup)(listView.getParent())).addView(empty);
        listView.setEmptyView(empty);
//        for (int i =0 ;i <10;i ++){
//            records.add(new MeetingAttendRecord());
//        }
        adapter.setList(records);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().pushFragmentToBackStack(PersonalMeetingDetailFragment.class,meetingSelectCondition);
            }
        });
        getMeetings();
    }

    private void getMeetings(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition.setUserId(config.getString("userId", ""));
        StringBuilder sb = new StringBuilder(Urls.MEETING_ATTEND);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",meetingSelectCondition.getPageNo())
                .praseToUrl("userId",meetingSelectCondition.getUserId())
                .praseToUrl("pageSize",meetingSelectCondition.getPageSize())
//                .praseToUrl("meetingName",meetingSelectCondition.getMeetingName())
//                .praseToUrl("date",meetingSelectCondition.getDate())
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
                        records.addAll(GsonUtil.fromJsonList(list, MeetingAttendRecord.class));
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
