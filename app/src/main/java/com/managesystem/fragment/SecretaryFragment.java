package com.managesystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.managesystem.R;
import com.wksc.framwork.activity.ZxingCaptureActivity;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/5.
 */
public class SecretaryFragment extends CommonFragment {
    @Bind(R.id.layout_scan)
    LinearLayout llScan;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_secretary, null);
        ButterKnife.bind(this, container);
        intView();
        return container;
    }

    private void intView() {
        setHeaderTitle(getStringFromResource(R.string.secretary));
        enableDefaultBack(false);
    }
    @OnClick({R.id.layout_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_scan:
                startActivity(ZxingCaptureActivity.class);
                break;
        }
    }

}
