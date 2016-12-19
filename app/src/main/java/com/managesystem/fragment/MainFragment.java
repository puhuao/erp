package com.managesystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.activity.EBookActivity;
import com.managesystem.activity.GoodNewsActivity;
import com.managesystem.activity.MainTainApplyActivity;
import com.managesystem.activity.MeetingManageActivity;
import com.managesystem.activity.PPSActivity;
import com.managesystem.activity.ResourceApplyActivity;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MainFragment extends CommonFragment {
    private String status;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.app_name));
        enableDefaultBack(false);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        status = config.getString("status","0");
    }

    @OnClick({R.id.layout_telephone_book,R.id.layout_communication,R.id.layout_meet_apply,R.id.layout_good_news,R.id.layout_resource_apply,R.id.layout_maintain})
    public void onClick(View v) {
        if (status.equals("0")){
            ToastUtil.showShortMessage(getContext(),"该账号还未激活");
        }else{
            switch (v.getId()) {
                case R.id.layout_maintain:
                    Bundle b = new Bundle();
                    b.putInt("type",1);
                    startActivity(MainTainApplyActivity.class,b);
                    break;
                case R.id.layout_meet_apply:
                    startActivity(MeetingManageActivity.class);
                    break;
                case R.id.layout_good_news:
                    startActivity(GoodNewsActivity.class);
                    break;
                case R.id.layout_resource_apply:
                    startActivity(ResourceApplyActivity.class);
                    break;
                case R.id.layout_communication:
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",1);
                    startActivity(PPSActivity.class,bundle);
                    break;
                case R.id.layout_telephone_book:
                    startActivity(EBookActivity.class);
                    break;
            }
        }
    }
}
