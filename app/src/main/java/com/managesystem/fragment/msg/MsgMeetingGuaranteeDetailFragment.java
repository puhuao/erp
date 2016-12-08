package com.managesystem.fragment.msg;

import android.content.DialogInterface;
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
import com.managesystem.widegt.CustomDialog;
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
 * 消息里会议保障提醒页面
 */
public class MsgMeetingGuaranteeDetailFragment extends CommonFragment {
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
        setHeaderTitle("会议保障通知");
        content.setText(message.content);
        fab.setText("确认");
//        if (message.status == 0){
//            CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
//            builder.setMessage("请确认工单");
//            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    updateDistribute();
//                }
//            });
//            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.create().show();
//        }
    }

    @OnClick({R.id.fab,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                updateDistribute();
                break;
        }
    }

    private void updateDistribute() {//确认接收派单
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MEETING_GUARANTEE_RATING);
        UrlUtils.getInstance(sb).praseToUrl("status", String.valueOf(2))
                .praseToUrl("rid", message.rid)
                .praseToUrl("userId",config.getString("userId", ""))
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
                    ToastUtil.showShortMessage(getContext(), "确认成功");
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
