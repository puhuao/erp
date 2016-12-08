package com.managesystem.fragment.wallet;

import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.GoodNewsAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.PaySuccessEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.fragment.goodnews.GoodNewsDetailFragment;
import com.managesystem.fragment.goodnews.GoodNewsSingInFragment;
import com.managesystem.fragment.loginAndRegister.ForgetPasswordFragment;
import com.managesystem.fragment.loginAndRegister.RegisterFragment;
import com.managesystem.fragment.meeting.MeetingDetailFragment;
import com.managesystem.fragment.meeting.MeetingGuaranteeInformationFragment;
import com.managesystem.fragment.msg.MsgNotReadFragment;
import com.managesystem.fragment.msg.MsgReadFragment;
import com.managesystem.model.Account;
import com.managesystem.model.GoodNews;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 我的消费页面
 */
public class MyWalletFragment extends CommonFragment implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.balance)
    TextView balance;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    PayRecordFragment payRecordFragment;
    ChargeRecordFragment chargeRecordFragment;
    Account account;
    FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_wallet, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @OnClick({R.id.dinner_pay,R.id.pay_another})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dinner_pay:
                dinnerPay(v);

                break;
            case R.id.pay_another:
                getContext().pushFragmentToBackStack(AnotherPayFragment.class,null);
                break;
        }
    }

    private void initView() {
        setHeaderTitle("我的钱包");
        payRecordFragment = new PayRecordFragment();
        fragmentList.add(payRecordFragment);
        chargeRecordFragment = new ChargeRecordFragment();
        fragmentList.add(chargeRecordFragment);
        fragmentPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragmentList);
        viewpager.setAdapter(fragmentPagerAdapter);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    if (payRecordFragment.isFirstLoad){
                        payRecordFragment.handler.sendEmptyMessage(0);
                    }
                }else if(position == 1){
                    if (chargeRecordFragment.isFirstLoad){
                        chargeRecordFragment.handler.sendEmptyMessage(0);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup.setOnCheckedChangeListener(this);
        getMyAccount();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.pay_record:
                viewpager.setCurrentItem(0);
                break;
            case R.id.charge_record:
                viewpager.setCurrentItem(1);
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;

        }

        private ArrayList<Fragment> fragmentsList;

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

    }
    private void getMyAccount(){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.MY_ACCOUNT);
        UrlUtils.getInstance(sb)
                .praseToUrl("userId",config.getString("userId", ""))
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
                        JSONArray jsonArray = new JSONArray(o);
                        String s = jsonArray.getString(0);
                        account = GsonUtil.fromJson(s,Account.class);
                        balance.setText(String.valueOf(account.getBalance()));
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

    private void dinnerPay(final View v){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.SAVE_PAY_RECORD);
        UrlUtils.getInstance(sb).praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("type","5")//用餐
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
                    CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                    builder.setTitle("用餐消费成功");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EventBus.getDefault().post(new PaySuccessEvent());
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        };
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Subscribe
    public void onEvent(PaySuccessEvent event){
        getMyAccount();
        payRecordFragment.handler.sendEmptyMessage(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
