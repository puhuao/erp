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
import com.managesystem.event.UpdateMyMeetingList;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 我的会议
 */
public class PersonalMeetingFragment extends CommonFragment {
    @Bind(R.id.tab_cursor)
    TabLayout tabCursor;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    MeetingApplyRecordFragment meetingApplyRecordFragment;
    MeetingAttendRecordFragment meetingAttendRecordFragment;
    NetFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.layout_tab_layout_view_pager, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter==null){
            adapter = new NetFragmentAdapter(getChildFragmentManager());
            viewpager.setAdapter(adapter);
            tabCursor.setupWithViewPager(viewpager);
        }
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.meeting_my));
        getTitleHeaderBar().setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().finish();
            }
        });
        mTitleList.add(getStringFromResource(R.string.meeting_apply_record));
        mTitleList.add(getStringFromResource(R.string.meeting_attend_record));
        meetingApplyRecordFragment  = new MeetingApplyRecordFragment();
        fragmentList.add(meetingApplyRecordFragment);
        meetingAttendRecordFragment = new MeetingAttendRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tupe",0);
        meetingAttendRecordFragment.setArguments(bundle);
        fragmentList.add(meetingAttendRecordFragment);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position ==0){
                    if (meetingApplyRecordFragment.isFirstLoad)
                        meetingApplyRecordFragment.handler.sendEmptyMessage(0);
                }else if(position ==1){
                    if (meetingAttendRecordFragment.isFirstLoad)
                        meetingAttendRecordFragment.handler.sendEmptyMessage(0);
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
    @Subscribe
    public void onEvent(UpdateMyMeetingList event){
        meetingApplyRecordFragment.handler.sendEmptyMessage(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
