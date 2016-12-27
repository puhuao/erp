package com.managesystem.fragment.workList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.managesystem.R;
import com.managesystem.activity.WorkListDetailActivity;
import com.managesystem.adapter.WorkListAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.WorkList;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 我的工单--已完成
 */
public class WorkListfinishFragment extends BaseListRefreshFragment<WorkList> {
    private ArrayList<WorkList> workLists = new ArrayList<>();
    WorkListAdapter adapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        hideTitleBar();
        adapter = new WorkListAdapter(getContext());
        adapter.setType(0);
        setData(workLists, adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=workLists.size()) {
                    Bundle bundle = new Bundle();

                    if (workLists.get(position).getServicetypeName().equals("会议")) {
                        bundle.putInt("type", 0);
                    } else {
                        bundle.putInt("type", 1);
                    }
                    bundle.putSerializable("obj", workLists.get(position));
                    startActivity(WorkListDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.WORK_LIST);
        UrlUtils.getInstance(sb).praseToUrl("status", "3")
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("pageNo", "1")
                .praseToUrl("pageSize", "20")
                .removeLastWord();
        excute(sb.toString(), WorkList.class);
    }


}
