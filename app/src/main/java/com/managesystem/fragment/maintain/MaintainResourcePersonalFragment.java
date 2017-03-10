package com.managesystem.fragment.maintain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.managesystem.R;
import com.managesystem.adapter.ResourcePersonAdapter;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.MeetingType;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8.
 * 我的物资
 */
public class MaintainResourcePersonalFragment extends BaseListRefreshFragment<ResourcePersonModel> {
    @Bind(R.id.send)
    Button sure;
    @Bind(R.id.fix)
    Button cancel;
    ResourcePersonAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    String userID;
    MeetingType meetingType;
    private boolean checkAll = false;

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
        sure.setText("取消");
        cancel.setText("确认");
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        getTitleHeaderBar().getRightTextView().setTextColor(
                getContext().getResources().getColor(R.color.text_hint));
        resourcePersonAdapter = new ResourcePersonAdapter(getContext());
        setData(resourcePersonModels, resourcePersonAdapter);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
                checkAll = !checkAll;
                if (checkAll){
                    getTitleHeaderBar().getRightTextView().setTextColor(
                            getContext().getResources().getColor(R.color.title_bar_right));
                }else{
                    getTitleHeaderBar().getRightTextView().setTextColor(
                            getContext().getResources().getColor(R.color.text_hint));
                }
                resourcePersonAdapter.setIsFromCheckAll(true);
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    if (checkAll){
                        r.isCheck = true;
                    }else{
                        r.isCheck = false;
                    }
                }
                resourcePersonAdapter.notifyDataSetChanged();
                resourcePersonAdapter.setIsFromCheckAll(false);
            }
        });
    }


    @OnClick({R.id.send, R.id.fix})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fix:
                //确认
                StringBuilder sb = new StringBuilder();
                StringBuilder ids = new StringBuilder();
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    if (r.isCheck) {
                        sb.append(r.getMaterialName() + ",");
                        ids.append(r.getMaterialnameId()+",");
                    }
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    ids.deleteCharAt(ids.length()-1);
                }else{
                    ToastUtil.showShortMessage(getContext(),"请选择设备");
                    break;
                }
                EventBus.getDefault().post(new MeetingTypeSelectEvent(meetingType, sb.toString(),ids.toString()));
                getContext().popToRoot(null);
                break;
            case R.id.send:
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
