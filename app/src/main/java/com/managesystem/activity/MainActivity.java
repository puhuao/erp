package com.managesystem.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.fragment.MainFragment;
import com.managesystem.fragment.MsgFragment;
import com.managesystem.fragment.SecretaryFragment;
import com.managesystem.fragment.TransferFragment;
import com.managesystem.widegt.CustomDialog;
import com.managesystem.widegt.CustomViewPager;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.ActivityManager;
import com.wksc.framwork.baseui.activity.BaseFragmentActivity;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by puhua on 2016/5/26.
 *
 * @
 */
public class MainActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.viewPager_history)
    CustomViewPager mViewPager;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup.setOnCheckedChangeListener(this);
        config = BaseApplication.getInstance().getCurrentConfig();
    }
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
}
