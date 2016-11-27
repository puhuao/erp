package com.managesystem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.UpdateMsgListEvent;
import com.managesystem.fragment.meeting.MeetingMSGDetailFragment;
import com.managesystem.fragment.msg.MsgMeetingGuaranteeDetailFragment;
import com.managesystem.fragment.msg.MsgNoticeFragment;
import com.managesystem.jpush.MsgMeetingFinishFragment;
import com.managesystem.model.Message;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.activity.CommonActivity;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/20.
 */
public class MeetingMsgDetailActivity  extends CommonActivity {
    IConfig config;
    Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        message = (Message) getIntent().getSerializableExtra("obj");
        if (!StringUtils.isBlank(message.type)){
            if (message.status==0){
                updateMessageStatus(1);//1：已查看，0：未查看
            }
            if (message.type.equals(Message.MEETING_REMIND)){
                //会议提醒
                pushFragmentToBackStack(MsgNoticeFragment.class, message);
//                pushFragmentToBackStack(MeetingMSGDetailFragment.class, message);
            }else if(message.type.equals(Message.REGISTER_NOTICE)){
                //新用户注册，提示管理员审核
                pushFragmentToBackStack(MsgNoticeFragment.class, message);
            }else if(message.type.equals(Message.WORK_LIST_REMIND)){
                //工单提醒，提醒管理员
                pushFragmentToBackStack(MsgNoticeFragment.class, message);
            }else if(message.type.equals(Message.MEETING_NOTICE)){
                //会议通知（可以报名）
                pushFragmentToBackStack(MeetingMSGDetailFragment.class, message);
//                pushFragmentToBackStack(MsgNoticeFragment.class, message);
            }else if(message.type.equals(Message.DESPATCH_NOTICE)){
                //派单通知（确认）
                pushFragmentToBackStack(MsgMeetingGuaranteeDetailFragment.class, message);
            }else if(message.type.equals(Message.MEETING_FINISH)){
                //会议工单完成，去评价
                pushFragmentToBackStack(MsgMeetingFinishFragment.class, message);
            }else if(message.type.equals(Message.WORK_LIST_FINISH)){
                //工单完成，去评价
                pushFragmentToBackStack(MsgMeetingFinishFragment.class, message);
            }else if(message.type.equals(Message.COMMENT_FINISH)){
                //评价完成，提示就行
                pushFragmentToBackStack(MsgNoticeFragment.class, message);
            }else{
                pushFragmentToBackStack(MsgNoticeFragment.class, message);
            }
        }else{
            ToastUtil.showShortMessage(this,"系统错误");
            finish();
        }

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }

    private void updateMessageStatus(int statu){
        StringBuilder sb = new StringBuilder(Urls.UPDATE_MSG_STATUS);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        if (message.messageId!=0){
            UrlUtils.getInstance(sb).praseToUrl("userId",config.getString("userId", ""))
                    .praseToUrl("type",message.type)
                    .praseToUrl("rid",message.rid)
                    .praseToUrl("status",String.valueOf(statu))
                    .removeLastWord();
        }else{
            UrlUtils.getInstance(sb).praseToUrl("messageId",String.valueOf(message.messageId))
                    .praseToUrl("status",String.valueOf(statu))
                    .removeLastWord();
        }


        DialogCallback callback = new DialogCallback<String>(this, String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(MeetingMsgDetailActivity.this,"网络错误");
            }
            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
//                    ToastUtil.showShortMessage(MeetingMsgDetailActivity.this,"通知所有用户成功");
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new UpdateMsgListEvent());
    }
}
