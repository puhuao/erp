package com.managesystem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.fragment.workList.MeetingGuaranteeWorkFragment;
import com.managesystem.fragment.workList.WorkListDetailFragment;
import com.managesystem.fragment.workList.WorkListFragment;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.WorkList;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class WorkListDetailActivity extends CommonActivity {

    IConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        int type=  getIntent().getExtras().getInt("type");
        WorkList workList = (WorkList) getIntent().getExtras().getSerializable("obj");
        if (type == 0){
            getMeetings(workList.getRid());
        }else{
            pushFragmentToBackStack(WorkListDetailFragment.class,workList);
        }

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }


    private void getMeetings(String id){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo","1")
                .praseToUrl("pageSize","20")
                .praseToUrl("isQueryDetail","1")
                .praseToUrl("meetingId",id)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(WorkListDetailActivity.this, String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(WorkListDetailActivity.this,"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        List<MeetingApplyRecord> applyRecords = new ArrayList<>();
                        applyRecords.addAll(GsonUtil.fromJsonList(list,MeetingApplyRecord.class));
                        MeetingApplyRecord  meetingApplyRecord = applyRecords.get(0);
                        pushFragmentToBackStack(MeetingGuaranteeWorkFragment.class,meetingApplyRecord);
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
