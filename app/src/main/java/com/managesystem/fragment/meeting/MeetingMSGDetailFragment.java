package com.managesystem.fragment.meeting;

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
import com.managesystem.model.Message;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/5.
 * 会议详情
 */
public class MeetingMSGDetailFragment extends CommonFragment {
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
        setHeaderTitle("会议消息通知");
        content.setText(message.content);
        if (message.status==1){
            fab.setBackgroundColor(getContext().getResources().getColor(R.color.text_hint));
            fab.setEnabled(false);
        }
    }

    @OnClick({R.id.fab,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                signUp();
                break;
        }
    }

    public void signUp(){

        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                        ToastUtil.showShortMessage(getContext(),"会议报名成功");
                    fab.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getContext().finish();
                        }
                    },1000);
                }
            }
        };
        StringBuilder sb = new StringBuilder(Urls.MEETING_ADD_USERS);
        UrlUtils.getInstance(sb).praseToUrl("meetingId",message.rid)
                .praseToUrl("type","1")
                .praseToUrl("userIds",config.getString("userId", ""))
                .removeLastWord();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
