package com.managesystem.fragment.phoneBooke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.managesystem.R;
import com.managesystem.model.PersonalInfo;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.widget.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 * 通信录_详情页面
 */
public class PhoneBookDetailDetailFragment extends CommonFragment {
    @Bind(R.id.tv_department)
    TextView tvDepartment;
    @Bind(R.id.name)
    TextView tvName;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.tv_cPhone)
    TextView tvCPhone;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.header)
    CircleImageView header;
    PersonalInfo personalInfo;
    private IConfig config;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_phone_book_detail, null);
        ButterKnife.bind(this, container);
        personalInfo = (PersonalInfo) getmDataIn();
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("联系人信息");
        tvName.setText(personalInfo.getName());
        tvDepartment.setText(personalInfo.getDepartmentName());
        tvCPhone.setText(personalInfo.getCphone());

        if (config.getBoolean("ispublish",false)){
            StringBuilder sb  =new StringBuilder();
            if (!StringUtils.isBlank(personalInfo.getPhone())&& personalInfo.getPhone().length() > 6){
                for (int i = 0; i < personalInfo.getPhone().length(); i++) {
                    char c = personalInfo.getPhone().charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
            }
            tvPhone.setText(sb.toString());
        }else{
            tvPhone.setText(personalInfo.getPhone());
        }

        tvState.setText(personalInfo.getStationName());
        Glide.with(getContext())
                .load(config.getString("headerIcon", "")).crossFade()
                .placeholder(R.drawable.ic_header_defalt)
                .error(R.drawable.ic_header_defalt)
                .thumbnail(0.1f).centerCrop()
                .into(header);
    }
}
