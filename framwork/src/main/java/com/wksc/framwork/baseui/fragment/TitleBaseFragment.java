package com.wksc.framwork.baseui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wksc.framwork.R;


/**
 * Created by wanglin on 2015/6/3.
 */
public abstract class TitleBaseFragment extends CubeFragment{
        protected TitleHeaderBar mTitleHeaderBar;
        protected LinearLayout mContentContainer;
    protected Boolean enableDefaultBack = true;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            ViewGroup view = (ViewGroup) inflater.inflate(getFrameLayoutId(), null);
            LinearLayout contentContainer = (LinearLayout) view.findViewById(R.id.view_base_frame_content);

            mTitleHeaderBar = (TitleHeaderBar) view.findViewById(R.id.view_base_frame_title_header);
            if (enableDefaultBack) {
                mTitleHeaderBar.setLeftOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            } else {
                mTitleHeaderBar.getLeftViewContainer().setVisibility(View.INVISIBLE);
            }
            mContentContainer = contentContainer;
            View contentView = createView(inflater, view, savedInstanceState);
            contentView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            contentContainer.addView(contentView);
            return view;
        }

        protected int getFrameLayoutId() {
            return R.layout.ui_base_content_frame_with_header_bar;
        }

        /**
         * make it looks like Activity
         */
        private void onBackPressed() {
            getContext().onBackPressed();
        }

        protected void enableDefaultBack(Boolean enableDefaultBack) {
            if (!enableDefaultBack){
                mTitleHeaderBar.getLeftViewContainer().setVisibility(View.INVISIBLE);
            }
            this.enableDefaultBack = enableDefaultBack;
        }

        protected void setHeaderTitle(int id) {
            mTitleHeaderBar.getTitleTextView().setText(id);
        }

        protected void setHeaderTitle(String title) {
            mTitleHeaderBar.getTitleTextView().setText(title);
        }

        public TitleHeaderBar getTitleHeaderBar() {
            return mTitleHeaderBar;
        }
}
