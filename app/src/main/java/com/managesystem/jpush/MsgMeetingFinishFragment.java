package com.managesystem.jpush;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.maintain.MaintainApplyRecordFragment;
import com.managesystem.fragment.maintain.MaintainDetailFragment;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.fragment.msg.MSGMaintainDetailFragment;
import com.managesystem.fragment.msg.MsgMeetingGuaranteeInformationFragment;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.Message;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 消息里会议工单完成提醒，去评价页面
 */
public class MsgMeetingFinishFragment extends CommonFragment {
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.fab)
    Button fab;
    Message message;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_msg_detail, null);
        ButterKnife.bind(this, container);
        message = (Message) getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(message.title);
        content.setText(message.content);
        fab.setText("去评价");
        if (message.status == 1) {
            fab.setBackgroundColor(getContext().getResources().getColor(R.color.text_hint));
            fab.setEnabled(false);
        }
    }

    @OnClick({R.id.fab,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                getContext().popTopFragment(null);
                if (message.type.equals(Message.MEETING_FINISH)){
                    getMeetings();
                }else{
//                    getContext().pushFragmentToBackStack(MaintainApplyRecordFragment.class, 0);
                    getContext().pushFragmentToBackStack(MSGMaintainDetailFragment.class,message.rid);
                }
                break;
        }
    }

    private MeetingApplyRecord meetingApplyRecord;
    ArrayList<MeetingApplyRecord> applyRecords = new ArrayList<>();

    private void getMeetings() {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", "1")
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("pageSize", "20")
                .praseToUrl("isQueryDetail", "1")
                .praseToUrl("meetingId", message.rid)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        applyRecords.addAll(GsonUtil.fromJsonList(list, MeetingApplyRecord.class));
                        if (applyRecords.size() > 0){
                            meetingApplyRecord = applyRecords.get(0);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("key", meetingApplyRecord);
                            MeetingGuaranteeInformationFragment guaranteeInformationFragment = new MeetingGuaranteeInformationFragment();
                            guaranteeInformationFragment.setArguments(bundle);
                            getContext().pushFragmentToBackStack(MsgMeetingGuaranteeInformationFragment.class, meetingApplyRecord);
                        }else{
                            getContext().finish();
                            ToastUtil.showShortMessage(getContext(),"网络错误");
                        }
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
