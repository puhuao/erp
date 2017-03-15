package com.managesystem.fragment.phoneBooke;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.managesystem.R;
import com.managesystem.config.Urls;
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
    ImageView header;
    @Bind(R.id.tv_office)
            TextView tvOffice;
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
        tvOffice.setText(personalInfo.getOfficeNo());
        tvPhone.setText(personalInfo.getPhone());
//        if (config.getString("position","").equals("院领导")){
//
//        }else{
//            if (personalInfo.getIspublish()==0){
//                StringBuilder sb  =new StringBuilder();
//                if (!StringUtils.isBlank(personalInfo.getPhone())&& personalInfo.getPhone().length() >= 6){
//                    for (int i = 0; i < personalInfo.getPhone().length(); i++) {
//                        char c = personalInfo.getPhone().charAt(i);
//                        if (i >= 3 && i <= 6) {
//                            sb.append('*');
//                        } else {
//                            sb.append(c);
//                        }
//                    }
//                }
//                tvPhone.setText(sb.toString());
//            }else{
//                tvPhone.setText(personalInfo.getPhone());
//            }
//        }
        tvState.setText(personalInfo.getStationName());
        String headPic = null;
        if (!StringUtils.isBlank(personalInfo.getHeadPic())){
            headPic = Urls.GETPICS+personalInfo.getHeadPic();
        }
        Glide.with(getContext()).load(headPic)
                .asBitmap().centerCrop()
                .error(R.drawable.ic_header_defalt)
                .placeholder(R.drawable.ic_header_defalt)
                .into(new BitmapImageViewTarget(header) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        header.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
