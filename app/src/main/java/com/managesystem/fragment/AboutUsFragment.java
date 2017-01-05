package com.managesystem.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2016/11/8.
 * 关于我们
 */
public class AboutUsFragment extends CommonFragment {
    @Bind(R.id.version)
    TextView version;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_about_us, null);
        ButterKnife.bind(this, container);
        setHeaderTitle("关于我们");
        PackageManager manager = getActivity().getPackageManager();
        try {

            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = info.versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return container;
    }
}
