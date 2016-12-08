package com.managesystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.GridImageAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.OnDepartmentModifyedEvent;
import com.managesystem.model.PersonalInfo;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by puhua on 2016/12/5.
 *
 * @
 */

public class PersonalHeadicUploadFragment extends CommonFragment {
    @Bind(R.id.gridview)
    GridView gridView;
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> images;
    GridImageAdapter gridImageAdapter;
    private IConfig config;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_head_icon_upload, null);
        ButterKnife.bind(this, container);
        config = BaseApplication.getInstance().getCurrentConfig();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("修改头像");
        gridImageAdapter = new GridImageAdapter(getContext());
        gridImageAdapter.excute();
    }

    @OnClick({ R.id.img_select, R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                modify(gridImageAdapter.sb.toString());
                break;
            case R.id.img_select:
                imagePicker = ImagePicker.getInstance();
                imagePicker.setMultiMode(false);
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setShowCamera(true);
                imagePicker.setSelectLimit(1);
                imagePicker.setCrop(true);
                imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                Intent intent = new Intent(getContext(), ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        gridImageAdapter.remove();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                gridImageAdapter.setList(images);
                gridImageAdapter.setImagePicker(imagePicker);
                gridView.setAdapter(gridImageAdapter);
                gridView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridImageAdapter.upload(gridView);
                    }
                }, 1000);

            } else {
                Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void modify(String headPic){
        StringBuilder sb = new StringBuilder(Urls.SAVE_USER);
        String s = UrlUtils.getInstance(sb)
                .praseToUrl("userId",config.getString("userId",""))
                .praseToUrl("headPic", headPic)
                .praseToUrl("type","2")
                .removeLastWord();
        DialogCallback callback = new DialogCallback<PersonalInfo>(getContext(), PersonalInfo.class) {

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, PersonalInfo o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"修改成功");
                    config.setString("roleName",o.getRoleName());
                    config.setString("name",o.getName());
                    config.setString("stationName",o.getStationName());
                    config.setString("department",o.getDepartmentName());
                    config.setString("cphone",o.getCphone());
                    config.setString("headerIcon",Urls.GETPICS+o.getHeadPic());
                    config.setBoolean("isLogin",true);
                    config.setString("phone",o.getPhone());
                    config.setString("sign",o.getSign());
                    EventBus.getDefault().post(new OnDepartmentModifyedEvent());
                    getContext().popTopFragment(null);
                }
            }
        };
        OkHttpUtils.post(s)//
                .tag(this)//
                .execute(callback);
    }

}
