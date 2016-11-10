package com.managesystem.fragment.meeting;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 我的会议——会议详情
 */
public class PersonalMeetingDetailFragment extends CommonFragment {
    @Bind(R.id.tab_cursor)
    TabLayout tabCursor;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_tab_layout_view_pager, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.meeting_my));
        mTitleList.add(getStringFromResource(R.string.meeting_detail));
        mTitleList.add(getStringFromResource(R.string.meeting_guarantee_information));
        MeetingDetailFragment meetingDetailFragment = new MeetingDetailFragment();
        fragmentList.add(meetingDetailFragment);
        MeetingGuaranteeInformationFragment guaranteeInformationFragment = new MeetingGuaranteeInformationFragment();
        fragmentList.add(guaranteeInformationFragment);
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
