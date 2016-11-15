package com.managesystem.fragment.resource;

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

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.fragment.meeting.MeetingDetailFragment;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 物资挂失
 */
public class PersonalResourceLostFragment extends CommonFragment {
    @Bind(R.id.tab_cursor)
    TabLayout tabCursor;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private MeetingSelectCondition meetingSelectCondition;

    private MeetingApplyRecord meetingApplyRecord;
    ArrayList<MeetingApplyRecord> applyRecords = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_tab_layout_view_pager, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        meetingSelectCondition = (MeetingSelectCondition) getmDataIn();
        setHeaderTitle(getStringFromResource(R.string.resource_lost));
        mTitleList.add(getStringFromResource(R.string.resource_lost_apply));
        mTitleList.add(getStringFromResource(R.string.resource_lost_list));
        ResourceLostApplyFragment resourceLostApplyFragment = new ResourceLostApplyFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("key",meetingApplyRecord);
//        meetingDetailFragment.setArguments(bundle);
        fragmentList.add(resourceLostApplyFragment);
        ResourceLostApplyListFragment resourceLostApplyListFragment = new ResourceLostApplyListFragment();
//        guaranteeInformationFragment.setArguments(bundle);
        fragmentList.add(resourceLostApplyListFragment);
        NetFragmentAdapter adapter = new NetFragmentAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tabCursor.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }

    public class NetFragmentAdapter extends FragmentPagerAdapter {

        public NetFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList == null ? null : fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

}
