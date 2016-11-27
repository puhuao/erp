package com.managesystem.fragment.resource;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okhttpserver.upload.UploadManager;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.PostRequest;
import com.managesystem.R;
import com.managesystem.adapter.GridImageAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.ResNameSelectEvent;
import com.managesystem.event.ResTypeSelectEvent;
import com.managesystem.model.MeetingType;
import com.managesystem.model.ResourceName;
import com.managesystem.model.ResourceType;
import com.managesystem.popupwindow.ResourceNameSelectPopupwindow;
import com.managesystem.popupwindow.ResourceTypeSelectPopupwindow;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 物资申请单
 */
public class ResourceApplyFragment extends CommonFragment {
@Bind(R.id.type)
    TextView type;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.gridview)
    GridView gridView;

    private ArrayList<ResourceName> resourceNames = new ArrayList<>();
    private ArrayList<ResourceType> resourceTypes = new ArrayList<>();
    private ResourceName resourceName;
    private ResourceType resourceType;
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
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_apply, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }


    @Subscribe
    public void onEvent(ResTypeSelectEvent event){
        resourceType = event.getResourceName();
        type.setText(event.getResourceName().getMaterialTypeName());

    }

    @Subscribe
    public void onEvent(ResNameSelectEvent event){
        resourceName = event.getResourceName();
        name.setText(resourceName.getMaterialName());

    }
    private void initView() {
        hideTitleBar();
        gridImageAdapter = new GridImageAdapter(getContext());
        gridImageAdapter.excute();
    }

    @OnClick({R.id.type,R.id.name,R.id.img_select,R.id.fab})
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
            case R.id.type:
                if (resourceTypes.size()==0){
                    getResourceTyps(v);
                }else{
                    ResourceTypeSelectPopupwindow resourceTypeSelectPopupwindow =
                            new ResourceTypeSelectPopupwindow(getContext(),resourceTypes);
                    resourceTypeSelectPopupwindow.showPopupwindow(v);
                }
                break;
            case R.id.name:
                if (resourceType==null){
                    ToastUtil.showShortMessage(getContext(),"清闲选择物资类型");
                    break;
                }
//                if (resourceNames.size() == 0){
                    getResourceName(v);
//                }else{
//                    ResourceNameSelectPopupwindow resourceNameSelectPopupwindow = new ResourceNameSelectPopupwindow(getContext(),resourceNames);
//                    resourceNameSelectPopupwindow.showPopupwindow(v);
//                }
                break;
        }
    }

    private void getResourceTyps(final View v){
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_TYPE);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    resourceTypes.addAll(GsonUtil.fromJsonList(o,ResourceType.class));
                    ResourceTypeSelectPopupwindow resourceTypeSelectPopupwindow =
                            new ResourceTypeSelectPopupwindow(getContext(),resourceTypes);
                    resourceTypeSelectPopupwindow.showPopupwindow(v);
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void getResourceName(final View v){
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_NAME);
        UrlUtils.getInstance(sb).praseToUrl("materialtypeId",resourceType.getMaterialtypeId());
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    resourceNames.clear();
                    resourceNames.addAll(GsonUtil.fromJsonList(o,ResourceName.class));
                    ResourceNameSelectPopupwindow resourceNameSelectPopupwindow =
                            new ResourceNameSelectPopupwindow(getContext(),resourceNames);
                    resourceNameSelectPopupwindow.showPopupwindow(v);
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    private void apply(){
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_APPLY);//物资申请
        sb.append("?");
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        UrlUtils.getInstance(sb) .praseToUrl("type", "1") .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("remark", "1") .praseToUrl("materialnameId",resourceName.getMaterialnameId())
                .praseToUrl("materialtypeId", resourceType.getMaterialtypeId())
                .praseToUrl("picurl", gridImageAdapter.sb.toString())
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"物资申请成功");
                    getContext().popTopFragment(null);
                }
            }
        };
        if (gridImageAdapter.sb.length()>0){
            gridImageAdapter.sb.deleteCharAt(gridImageAdapter.sb.length()-1);
        }
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
//                .params("type", "1")
//                .params("userId", config.getString("userId", ""))
//                .params("remark", "")
//                .params("materialnameId",resourceName.getMaterialnameId())
//                .params("materialtypeId",resourceType.getMaterialtypeId())
//                .params("picurl", gridImageAdapter.sb.toString()) // 这里支持一个key传多个文件
                .execute(callback);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                gridImageAdapter.upload();
            } else {
                Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
