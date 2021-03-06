package com.managesystem.fragment;

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
import com.managesystem.fragment.msg.MsgNotReadFragment;
import com.managesystem.fragment.msg.MsgReadFragment;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 消息页面
 */
public class MsgFragment extends CommonFragment {
    @Bind(R.id.tab_cursor)
    TabLayout tabCursor;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    MsgNotReadFragment msgNotReadFragment;
     MsgReadFragment msgReadFragment;
    NetFragmentAdapter adapter;
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
            viewpager.setCurrentItem(0);
            getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgNotReadFragment.markAllRead();
                }
            });
            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position==0){
                        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
                        if (msgNotReadFragment.isFirstLoad){
                            msgNotReadFragment.handler.sendEmptyMessage(0);
                        }
                    }else if(position == 1){
                        getTitleHeaderBar().getRightViewContainer().setVisibility(View.GONE);
                        if (msgReadFragment.isFirstLoad){
                            msgReadFragment.handler.sendEmptyMessage(0);
                        }
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public void postToLoadData(){
            msgNotReadFragment.stopRefresh();
    }


    private void initView() {
        enableDefaultBack(false);
        getTitleHeaderBar().setRightText("一键阅读");
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        setHeaderTitle(getStringFromResource(R.string.msg));
        mTitleList.add(getStringFromResource(R.string.msg_read_not));
        mTitleList.add(getStringFromResource(R.string.msg_read));
        msgNotReadFragment = new MsgNotReadFragment();
        fragmentList.add(msgNotReadFragment);
        msgReadFragment = new MsgReadFragment();
        fragmentList.add(msgReadFragment);

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
