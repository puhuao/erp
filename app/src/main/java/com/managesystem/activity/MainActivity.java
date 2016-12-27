package com.managesystem.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnMSGNoticeEvent;
import com.managesystem.fragment.MainFragment;
import com.managesystem.fragment.MsgFragment;
import com.managesystem.fragment.SecretaryFragment;
import com.managesystem.fragment.TransferFragment;
import com.managesystem.model.RemoteVersion;
import com.managesystem.tools.UrlUtils;
import com.managesystem.update.UpdateManager;
import com.managesystem.widegt.CustomDialog;
import com.managesystem.widegt.CustomViewPager;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.ActivityManager;
import com.wksc.framwork.baseui.activity.BaseFragmentActivity;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.widget.BadgeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by puhua on 2016/5/26.
 *
 * @
 */
public class MainActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.viewPager_history)
    CustomViewPager mViewPager;
    @Bind(R.id.btn_msg)
    RadioButton btnMsg;
    @Bind(R.id.bt)
    TextView bt;
    @Bind(R.id.btn_transfer)
            RadioButton btnTrnasfer;
    @Bind(R.id.btn_secretary)
            RadioButton btnSecretary;
    List<Fragment> fragments = new ArrayList<>();
    IConfig config;

    private MainFragment homeFragment = new MainFragment();
    private TransferFragment transferFragment = new TransferFragment();
    private MsgFragment msgFragment = new MsgFragment();
    private SecretaryFragment secretaryFragment = new SecretaryFragment();
    FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        ActivityManager.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            getWindow().getDecorView().setSystemUiVisibility(options);

            getWindow().setStatusBarColor(getResources().getColor(com.wksc.framwork.R.color.black));

        }
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(MainActivity.this);
    }

    @Subscribe
    public void onEvent(OnMSGNoticeEvent event){
        badge.setTargetView(bt);
        badge.setTextColor(getResources().getColor(R.color.white));
        badge.setBadgeCount(event.number);
        badge.setBadgeMargin(6);
        badge.setBadgeGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);

    }
    BadgeView badge;
    private int currentVersionCode;
    private void initView(){
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            currentVersionCode = info.versionCode;
            checkVersion(String.valueOf(currentVersionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        badge = new BadgeView(this);
        fragments.add(homeFragment);
        fragments.add(transferFragment);
        fragments.add(msgFragment);
        fragments.add(secretaryFragment);
        fragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), (ArrayList<Fragment>) fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setPagingEnabled(false);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position ==1){
                    if (transferFragment.isFirstLoad)
                        transferFragment.handler.sendEmptyMessage(0);
                }else if(position ==2){
                    msgFragment.postToLoadData();
                }
             }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        config = BaseApplication.getInstance().getCurrentConfig();
        status = config.getString("status","0");
        if (status.equals("0")){
            btnMsg.setOnClickListener(this);
            btnSecretary.setOnClickListener(this);
            btnTrnasfer.setOnClickListener(this);
        }else{
            radioGroup.setOnCheckedChangeListener(this);
        }


    }
    String status;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.btn_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.btn_transfer:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.btn_msg:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.btn_secretary:
                mViewPager.setCurrentItem(3);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        ToastUtil.showShortMessage(MainActivity.this,"该账号还未激活");
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

    @Override
    public void onBackPressed() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("确认退出应用？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {

        secretaryFragment.onDestroy();
        homeFragment.onDestroy();
        msgFragment.onDestroy();
        transferFragment.onDestroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        JPushInterface.clearAllNotifications(this);

    }


    private void checkVersion(String versionCode) {
        StringBuilder sb = new StringBuilder(Urls.CHECK_VERSION);
        UrlUtils.getInstance(sb).praseToUrl("versionCode", versionCode)
                .praseToUrl("type", "1")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<RemoteVersion>(MainActivity.this, RemoteVersion.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(MainActivity.this, "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, RemoteVersion o, Request request, @Nullable Response response) {
                if (o != null) {
                    Boolean isNew = o.isIsNew();
                    if (!isNew){
                        RemoteVersion.NewVersionBean newVersionBean =  o.getNewVersion();
                        UpdateManager.getUpdateManager().setContext(MainActivity.this);
                        UpdateManager.getUpdateManager().setUpdateInfo(newVersionBean);
                        UpdateManager.getUpdateManager().showNoticeDialog();
                    }
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }
}
