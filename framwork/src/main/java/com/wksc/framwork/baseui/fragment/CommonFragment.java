package com.wksc.framwork.baseui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wksc.framwork.R;
import com.wksc.framwork.baseui.ActivityManager;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.widget.ProgressHUD;


/**
 * Created by wanglin on 2015/6/3.
 */
public abstract class CommonFragment extends TitleBaseFragment {

    public ProgressHUD mProgressHUD;
    public String extraParam = "";
    public int flag=0;
    public boolean isFirst = true;

    /**
     * 显示消息提示，避免重复提示
     *
     * @param msg
     */
    public void showMessage(String msg) {
        ToastUtil.showShortMessage(getContext(), msg);
    }


    /**
     * 以无参数的模式启动Activity。
     *
     * @param activityClass
     */
    public void  startActivity(Class<? extends Activity> activityClass) {
        getContext().startActivity(getLocalIntent(activityClass, null));
        getContext().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    /**
     * 以无参数的模式启动Activity。
     *
     * @param activityClass
     */
    public void  startActivityNoAnim(Class<? extends Activity> activityClass) {
        getContext().startActivity(getLocalIntent(activityClass, null));
    }

    /**
     * 以绑定参数的模式启动Activity。
     *
     * @param activityClass
     */
    public void startActivity(Class<? extends Activity> activityClass, Bundle bd) {
        getContext().startActivity(getLocalIntent(activityClass, bd));
        getContext().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    /**
     * 获取当前程序中的本地目标
     *
     * @param localIntent
     * @return
     */
    public Intent getLocalIntent(Class<? extends Context> localIntent, Bundle bd) {
        Intent intent = new Intent(getActivity(), localIntent);
        if (null != bd) {
            intent.putExtras(bd);
        }

        return intent;
    }

    /**
     * 显示progress对话框
     */
    protected void showProgress(final String msg) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mProgressHUD = ProgressHUD.show(getContext(), msg, true, true, null);
            }
        });
    }

    /**
     * 取消progress对话框
     */
    protected void disProgress() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (null != mProgressHUD)
                    mProgressHUD.dismiss();
            }

        });

    }

    /**
     * 执行返回操作
     */
    protected void goBack(){
        getContext().onBackPressed();
    }

    /**
     * 执行返回操作，并且回传参数
     */
    protected  void goBackWithParams(Object param){
        getContext().popTopFragment(param);
    }

    protected void hideTitleBar(){
        mTitleHeaderBar.setVisibility(View.GONE);
    }

    protected void hideLeftButton(){
        mTitleHeaderBar.getLeftViewContainer().setVisibility(View.GONE);
    }

    protected void hideRightButton(){
        mTitleHeaderBar.getRightViewContainer().setVisibility(View.GONE);
    }

    protected void hideDefaultRightButton(){
        mTitleHeaderBar.getRightImageView().setVisibility(View.GONE);
    }

    protected void showRightButton(){
        mTitleHeaderBar.getRightViewContainer().setVisibility(View.VISIBLE);
    }

    protected ImageView getRightButton(){
        return mTitleHeaderBar.getRightImageView();
    }

    protected void hideSoftInput(View v){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected String getStringFromResource(int id){
        return getContext().getResources().getString(id);
    }
}
