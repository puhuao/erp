package com.managesystem.fragment.loginAndRegister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.managesystem.R;
import com.managesystem.activity.LoginActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 启动页面
 */
public class StartFragment extends CommonFragment {
    @Bind(R.id.img_start)
    ImageView imgStart;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_start, null);
        ButterKnife.bind(this, container);
        hideTitleBar();
        //初始化
        initView();
        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getContext().finish();
                startActivity(LoginActivity.class);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgStart.startAnimation(alphaAnimation);
        return container;
    }

    private void initView() {
    }
}
