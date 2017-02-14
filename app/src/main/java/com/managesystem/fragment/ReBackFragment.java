package com.managesystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.GridImageAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.ResNameSelectEvent;
import com.managesystem.event.ResTypeSelectEvent;
import com.managesystem.fragment.resource.ResourceNameSelectFragment;
import com.managesystem.fragment.resource.ResourceTypeSelectFragment;
import com.managesystem.model.ResourceName;
import com.managesystem.model.ResourceType;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 反馈
 */
public class ReBackFragment extends CommonFragment {
    @Bind(R.id.gridview)
    GridView gridView;
    @Bind(R.id.reback_content)
    EditText applyReason;

    private ImagePicker imagePicker;
    private ArrayList<ImageItem> images;
    GridImageAdapter gridImageAdapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_reback, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }



    private void initView() {
        setHeaderTitle("反馈");
        gridImageAdapter = new GridImageAdapter(getContext());
        gridImageAdapter.excute();
    }

    @OnClick({ R.id.img_select, R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                apply();
                break;
            case R.id.img_select:
                imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setShowCamera(true);
                imagePicker.setSelectLimit(9);
                imagePicker.setCrop(false);
                Intent intent = new Intent(getContext(), ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }


    private void apply() {
        StringBuilder sb = new StringBuilder(Urls.REBACK);//反馈
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        if (gridImageAdapter.sb.length() > 0) {
            gridImageAdapter.sb.deleteCharAt(gridImageAdapter.sb.length() - 1);
        }
        UrlUtils.getInstance(sb).praseToUrl("type", "1").praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("info", applyReason.getText().toString())
                .praseToUrl("pic", gridImageAdapter.sb.toString())
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "反馈成功");
                    getContext().popTopFragment(null);
                }
            }
        };
        if (gridImageAdapter.sb.length() > 0) {
            gridImageAdapter.sb.deleteCharAt(gridImageAdapter.sb.length() - 1);
        }
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
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


}
