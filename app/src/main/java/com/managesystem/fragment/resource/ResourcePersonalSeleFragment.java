package com.managesystem.fragment.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.managesystem.R;
import com.managesystem.adapter.ResourcePersonAdapter;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.event.ResourceSelectEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.MeetingType;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8.
 * 我的物资可供选择
 */
public class ResourcePersonalSeleFragment extends BaseListRefreshFragment<ResourcePersonModel> {
    @Bind(R.id.send)
    Button sure;
    @Bind(R.id.fix)
    Button cancel;
    ResourcePersonAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    String userID;
    MeetingType meetingType;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_person, null);
        ButterKnife.bind(this, container);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        meetingType = (MeetingType) getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        setHeaderTitle("我的设备");
        sure.setText("确认");
        cancel.setText("取消");
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        resourcePersonAdapter = new ResourcePersonAdapter(getContext());
        setData(resourcePersonModels, resourcePersonAdapter);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
                resourcePersonAdapter.setIsFromCheckAll(true);
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    r.isCheck = true;
                }
                resourcePersonAdapter.notifyDataSetChanged();
                resourcePersonAdapter.setIsFromCheckAll(false);
            }
        });
    }


    @OnClick({R.id.send, R.id.fix})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                //确认
                List<ResourcePersonModel> list = new ArrayList<>();
                if (list.size()>0){
                    ToastUtil.showShortMessage(getContext(),"只能选择一个设备挂失");
                    break;
                }
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    if (r.isCheck) {
                        list.add(r);
                    }
                }
                EventBus.getDefault().post(new ResourceSelectEvent(list));
                getContext().popTopFragment(null);
                break;
            case R.id.fix:
                //取消
                getContext().popTopFragment(null);
                break;
        }
    }


    @Override
    public void loadMore(int pageNo) {
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", String.valueOf(pageNo))
                .praseToUrl("userId", userID)
                .praseToUrl("pageSize", "20")
                .praseToUrl("keyword", "")
                .removeLastWord();
        excute(sb.toString(), ResourcePersonModel.class);
    }
}
