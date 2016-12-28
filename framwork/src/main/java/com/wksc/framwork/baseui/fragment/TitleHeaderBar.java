package com.wksc.framwork.baseui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wksc.framwork.R;


public class TitleHeaderBar extends RelativeLayout {
     private TextView mCenterTitleTextView;
     private ImageView mLeftReturnImageView;
     private ImageView mRightImageView;
     private TextView mRightTextView;
     private RelativeLayout mLeftViewContainer;
     private LinearLayout mRightViewContainer;
     private RelativeLayout mCenterViewContainer;
    private ImageView mLeftTextView;

     private String mTitle;

     public TitleHeaderBar(Context context) {
     super(context);
     }

     public TitleHeaderBar(Context context, AttributeSet attrs) {
     this(context, attrs, 0);
     }

     public TitleHeaderBar(Context context, AttributeSet attrs, int defStyleAttr) {
     super(context, attrs, defStyleAttr);

     LayoutInflater.from(context).inflate(getHeaderViewLayoutId(), this);
     mLeftViewContainer = (RelativeLayout) findViewById(R.id.ly_title_bar_left);
     mRightViewContainer = (LinearLayout) findViewById(R.id.ly_title_bar_right);
     mLeftReturnImageView = (ImageView) findViewById(R.id.iv_title_bar_left);
     mCenterTitleTextView = (TextView) findViewById(R.id.tv_title_bar_title);
     mRightImageView = (ImageView) findViewById(R.id.btn_right);
     mRightTextView = (TextView) findViewById(R.id.tv_right);
     mCenterViewContainer = (RelativeLayout) findViewById(R.id.ly_title_bar_center);
         mLeftTextView = (ImageView) findViewById(R.id.tv_left);
     }

     protected int getHeaderViewLayoutId() {
     return R.layout.ui_header_bar_title;
     }

     public ImageView getLeftImageView() {
     return mLeftReturnImageView;
     }

     public ImageView getRightImageView() {
     return mRightImageView;
     }

     public TextView getTitleTextView() {
     return mCenterTitleTextView;
     }

     public void setTitle(String title) {
     mTitle = title;
     mCenterTitleTextView.setText(title);
     }

     public String getTitle() {
     return mTitle;
     }

     private RelativeLayout.LayoutParams makeLayoutParams(View view) {
     ViewGroup.LayoutParams lpOld = view.getLayoutParams();
     RelativeLayout.LayoutParams lp = null;
     if (lpOld == null) {
     lp = new RelativeLayout.LayoutParams(-2, -1);
     } else {
     lp = new RelativeLayout.LayoutParams(lpOld.width, lpOld.height);
     }
     return lp;
     }

     /**
     * set customized view to left side
     *
     * @param view the view to be added to left side
     */
    public void setCustomizedLeftView(View view) {
        mLeftReturnImageView.setVisibility(GONE);
        RelativeLayout.LayoutParams lp = makeLayoutParams(view);
//        lp.addRule(CENTER_VERTICAL);
        lp.addRule(ALIGN_PARENT_LEFT);
        getLeftViewContainer().addView(view, lp);
    }

    /**
     * set customized view to left side
     *
     * @param layoutId the xml layout file id
     */
    public void setCustomizedLeftView(int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCustomizedLeftView(view);
    }

    /**
     * set customized view to center
     *
     * @param view the view to be added to center
     */
    public void setCustomizedCenterView(View view) {
        mCenterTitleTextView.setVisibility(GONE);
        RelativeLayout.LayoutParams lp = makeLayoutParams(view);
//        lp.addRule(CENTER_IN_PARENT);
        getCenterViewContainer().addView(view, lp);
    }

    /**
     * set customized view to center
     *
     * @param layoutId the xml layout file id
     */
    public void setCustomizedCenterView(int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCustomizedCenterView(view);
    }

    /**
     * set customized view to right side
     *
     * @param view the view to be added to right side
     */
    public void setCustomizedRightView(View view) {
        RelativeLayout.LayoutParams lp = makeLayoutParams(view);
//        lp.addRule(CENTER_VERTICAL);
        lp.addRule(ALIGN_PARENT_RIGHT);

        getRightViewContainer().addView(view, lp);
    }



    /**
     * set customized view to right
     *
     * @param layoutId the xml layout file id
     */
    public void setCustomizedRightView(int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCustomizedRightView(view);
    }

    public RelativeLayout getLeftViewContainer() {
        return mLeftViewContainer;
    }

    public RelativeLayout getCenterViewContainer() {
        return mCenterViewContainer;
    }

    public LinearLayout getRightViewContainer() {
        return mRightViewContainer;
    }

    public void setLeftOnClickListener(OnClickListener l) {
        mLeftViewContainer.setOnClickListener(l);
        mLeftTextView.setOnClickListener(l);
    }

    public void setCenterOnClickListener(OnClickListener l) {
        mCenterViewContainer.setOnClickListener(l);
    }

    public void setRightOnClickListener(OnClickListener l) {
        mRightViewContainer.setOnClickListener(l);
    }

    public void setRightImageClickListener(OnClickListener l){
        mRightImageView.setOnClickListener(l);
        mRightTextView.setOnClickListener(l);
    }

    public void setRightImageResource(int resId){
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(resId);
        mRightTextView.setVisibility(View.GONE);
    }

    public void setRightText(String rightText) {
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText(rightText);
        mRightImageView.setVisibility(View.GONE);
    }

    public void setLeftImageResource(int resId){
        mLeftReturnImageView.setImageResource(resId);
    }

    public TextView getRightTextView() {
        return mRightTextView;
    }

    public void setmLeftTextView(int id){
        mLeftTextView.setImageResource(id);
        mLeftTextView.setVisibility(VISIBLE);
        mLeftReturnImageView.setVisibility(GONE);
    }

    public void setRightBothShow() {
        mRightImageView.setVisibility(VISIBLE);
        mRightTextView.setVisibility(VISIBLE);
    }
}
