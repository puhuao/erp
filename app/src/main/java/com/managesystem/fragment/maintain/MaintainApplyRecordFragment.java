package com.managesystem.fragment.maintain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.adapter.MaintainApplyRecordAdapter;
import com.managesystem.config.Urls;
import com.managesystem.event.OnMainTainUpdata;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.Maintain;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 * 我的运维记录
 */
public class MaintainApplyRecordFragment extends BaseListRefreshFragment<Maintain> {
    MaintainApplyRecordAdapter adapter;
    ArrayList<Maintain> records = new ArrayList<>();
    int type = 0;

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

    private void initView() {
        isfirstFragment = true;
        type = (int) getmDataIn();
        setHeaderTitle("我的运维服务");
        getTitleHeaderBar().setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().finish();
            }
        });
        adapter = new MaintainApplyRecordAdapter(getContext());
        setData(records,adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=records.size())
                getContext().pushFragmentToBackStack(MaintainDetailFragment.class,records.get(position).getOrderId());
            }
        });
    }

    private int getnotComment() {
        int count = 0;
        for (int i = 0 ;i <records.size();i++){
            if (records.get(i).getStatus()==3){
                count++;
            }
        }
        return count;
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MAINTAIN_LIST_DETAIL);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("pageSize","20")
                .removeLastWord();
        excute(sb.toString(),Maintain.class);
    }

    @Subscribe
    public void onEvent(OnMainTainUpdata e){
        pageNo = 1;
        loadMore(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean processBackPressed() {
//        if (type ==1){
            getContext().finish();
//        }
        return true;
    }
}
