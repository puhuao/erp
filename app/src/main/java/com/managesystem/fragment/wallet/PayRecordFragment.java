package com.managesystem.fragment.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.activity.MeetingMsgDetailActivity;
import com.managesystem.adapter.AccountRecordAdapter;
import com.managesystem.adapter.MsgReadAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.AccountRecord;
import com.managesystem.model.Message;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 消费记录
 */
public class PayRecordFragment extends BaseListRefreshFragment<AccountRecord> {
    AccountRecordAdapter accountRecordAdapter;
    ArrayList<AccountRecord> accountRecords = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        hideTitleBar();
        accountRecordAdapter = new AccountRecordAdapter(getContext());
        accountRecordAdapter.setType(2);
        setData(accountRecords,accountRecordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=accountRecords.size()) {
//                    Intent i = new Intent(getContext(), MeetingMsgDetailActivity.class);
//                    AccountRecord accountRecord = accountRecords.get(position);
//                    i.putExtra("obj", accountRecord);
//                    getContext().startActivity(i);
                }
            }
        });
    }

    @Override
    public void loadMore(int pageNo) {//获取已读消息
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MY_ACCOUNT_RECORD);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .praseToUrl("type","2")//1：充值记录，2：消费记录
                .removeLastWord();
        excute(sb.toString(),AccountRecord.class);
    }

    public void stopAnimation(){
        stopRefresh();
    }
}
