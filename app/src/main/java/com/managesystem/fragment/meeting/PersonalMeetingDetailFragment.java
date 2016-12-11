package com.managesystem.fragment.meeting;

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
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingRoomDetail;
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
 * 我的会议——会议详情
 */
public class PersonalMeetingDetailFragment extends CommonFragment {
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
        setHeaderTitle(getStringFromResource(R.string.meeting_detail));
        mTitleList.add(getStringFromResource(R.string.meeting_detail));
        mTitleList.add(getStringFromResource(R.string.meeting_guarantee_information));

        getMeetings();
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
    private void getMeetings(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        meetingSelectCondition.setUserId(config.getString("userId", ""));
        StringBuilder sb = new StringBuilder(Urls.MEETING_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",meetingSelectCondition.getPageNo())
                .praseToUrl("userId",meetingSelectCondition.getUserId())
                .praseToUrl("pageSize",meetingSelectCondition.getPageSize())
                .praseToUrl("meetingName",meetingSelectCondition.getMeetingName())
                .praseToUrl("date",meetingSelectCondition.getDate())
                .praseToUrl("isQueryDetail","1")
                .praseToUrl("meetingId",meetingSelectCondition.getMeetingId())
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(o);
                        String list = jsonObject.getString("list");
                        applyRecords.addAll(GsonUtil.fromJsonList(list,MeetingApplyRecord.class));
                        meetingApplyRecord = applyRecords.get(0);

                        MeetingDetailFragment meetingDetailFragment = new MeetingDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("key",meetingApplyRecord);
                        meetingDetailFragment.setArguments(bundle);
                        fragmentList.add(meetingDetailFragment);
                        MeetingGuaranteeInformationFragment guaranteeInformationFragment = new MeetingGuaranteeInformationFragment();
                       guaranteeInformationFragment.setArguments(bundle);
                        fragmentList.add(guaranteeInformationFragment);
                        NetFragmentAdapter adapter = new NetFragmentAdapter(getChildFragmentManager());
                        viewpager.setAdapter(adapter);
                        tabCursor.setupWithViewPager(viewpager);
                        viewpager.setCurrentItem(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
