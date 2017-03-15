package com.managesystem.fragment.pps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.widget.ExpandGridView;
import com.managesystem.R;
import com.managesystem.adapter.GridImageAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.event.PPSListUpdateEvent;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.StringUtils;
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
 * Created by Administrator on 2016/11/5.
 * 发布新消息
 */
public class PublishPPSFragment extends CommonFragment {
    @Bind(R.id.theme)
    EditText theme;
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.gridview)
    ExpandGridView gridView;
    String sTheme, sContent;
    @Bind(R.id.fab)
    Button fab;
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> images;
    GridImageAdapter gridImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_pps_publish, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    @Subscribe
    public void onEvent(MeetingRoomSelectEvent event) {
    }

    @OnClick({R.id.fab, R.id.img_select})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_select:
                imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setShowCamera(true);
                imagePicker.setSelectLimit(9);
                imagePicker.setCrop(false);
                Intent intent = new Intent(getContext(), ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.fab:
                sTheme = theme.getText().toString();
//                if (StringUtils.isBlank(sTheme)) {
//                    ToastUtil.showShortMessage(getContext(), "请填写主题");
//                    return;
//                }
                sContent = content.getText().toString();
                if (StringUtils.isBlank(sContent)) {
                    ToastUtil.showShortMessage(getContext(), "请填写话题内容");
                    return;
                }
                apply();
                break;
        }
    }

    private void initView() {
        setHeaderTitle("发布新话题");
        gridImageAdapter = new GridImageAdapter(getContext());
        gridImageAdapter.excute();
    }

    private void apply() {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        if (gridImageAdapter.sb.length() > 0) {
            gridImageAdapter.sb.deleteCharAt(gridImageAdapter.sb.length() - 1);
        }
        StringBuilder sb = new StringBuilder(Urls.PPS_POST);
        UrlUtils.getInstance(sb).praseToUrl("title", "话题")
                .praseToUrl("userId", config.getString("userId", ""))
                .praseToUrl("content", sContent)
                .praseToUrl("pics", gridImageAdapter.sb.toString())
                .removeLastWord();
//        sb.append("&title=");
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    ToastUtil.showShortMessage(getContext(), "发布新消息成功");
                    EventBus.getDefault().post(new PPSListUpdateEvent());
                    getContext().finish();
                }
            }
        };

        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
