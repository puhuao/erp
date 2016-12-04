package com.managesystem.fragment.workList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.WorkListDetailActivity;
import com.managesystem.adapter.WorkListAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.WorkListFinishEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.fragment.meeting.MeetingDetailFragment;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.model.WorkList;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 我的工单--未完成
 */
public class WorkListUnfinishFragment extends BaseListRefreshFragment<WorkList> {
    @Bind(R.id.list_view)
    ListView listView;
    View empty;
    private ArrayList<WorkList> workLists = new ArrayList<>();
    WorkListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_swipe_refresh_list, null);
        empty = inflater.inflate(R.layout.empty_view, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        hideTitleBar();
        adapter = new WorkListAdapter(getContext());
        adapter.setType(1);
        setData(workLists, adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != workLists.size()) {
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

    @Subscribe
    public void onEvent(WorkListFinishEvent event) {
        pageNo = 1;
        loadMore(1);
    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.WORK_LIST);
        UrlUtils.getInstance(sb).praseToUrl("status", "2")
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("pageNo", String.valueOf(pageNo))
                .praseToUrl("pageSize", "20")
                .removeLastWord();
        excute(sb.toString(), WorkList.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
