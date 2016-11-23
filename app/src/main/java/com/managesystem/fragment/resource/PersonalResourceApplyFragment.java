package com.managesystem.fragment.resource;

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
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingSelectCondition;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 物资申请
 */
public class PersonalResourceApplyFragment extends CommonFragment {
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
        setHeaderTitle(getStringFromResource(R.string.resource_apply));
        mTitleList.add(getStringFromResource(R.string.resource_apply_title));
        mTitleList.add(getStringFromResource(R.string.resource_apply_list));
        final ResourceApplyFragment resourceLostApplyFragment = new ResourceApplyFragment();
        fragmentList.add(resourceLostApplyFragment);
        final ResourceApplyListFragment resourceLostApplyListFragment = new ResourceApplyListFragment();
        fragmentList.add(resourceLostApplyListFragment);
        NetFragmentAdapter adapter = new NetFragmentAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tabCursor.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    if (resourceLostApplyListFragment.isFirstLoad){
                        resourceLostApplyListFragment.handler.sendEmptyMessage(0);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
