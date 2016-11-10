package com.managesystem.fragment.loginAndRegister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.managesystem.R;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 */
public class ForgetPasswordFragment extends CommonFragment {
    @Bind(R.id.fab)
    Button fab;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_register, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle(getStringFromResource(R.string.page_name_forget_password));
        fab.setText(getStringFromResource(R.string.confirm));
    }
}
