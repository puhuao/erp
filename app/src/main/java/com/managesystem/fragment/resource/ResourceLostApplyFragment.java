package com.managesystem.fragment.resource;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.managesystem.event.ResourceSelectEvent;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.tools.GlideImageLoader;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 * 物资挂失申请单
 */
public class ResourceLostApplyFragment extends CommonFragment {
@Bind(R.id.ll_type)
    LinearLayout ll;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.gridview)
    GridView gridView;

    List<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    ResourcePersonModel resourcePersonModel;
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
    public void onEvent(ResourceSelectEvent event){
        resourcePersonModels = event.getModel();
        StringBuilder sb = new StringBuilder();
        for (ResourcePersonModel r :
                resourcePersonModels) {
            sb.append(r.getMaterialtypeName()+"、");
        }
        if (sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
        name.setText(sb);

    }

    private void initView() {
        hideTitleBar();
        ll.setVisibility(View.GONE);
        gridImageAdapter = new GridImageAdapter(getContext());
        gridImageAdapter.excute();
    }

    @OnClick({R.id.name,R.id.fab,R.id.img_select})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                getContext().pushFragmentToBackStack(ResourcePersonalSeleFragment.class,null);
                break;
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

    private void apply(){
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_APPLY);//物资申请
        sb.append("?");
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
       UrlUtils urlUtils =  UrlUtils.getInstance(sb) .praseToUrl("type", "2") .praseToUrl("userId",config.getString("userId", ""))
                .praseToUrl("remark", "2") ;
        for (ResourcePersonModel r :
                resourcePersonModels) {
            urlUtils.praseToUrl("materialIds", r .getMaterialId());
        }
        urlUtils.praseToUrl("picurl", gridImageAdapter.sb.toString()); // 这里支持一个key传多个文件)
        urlUtils .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(),"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(getContext(),"物资挂失成功");
                    getContext().popTopFragment(null);
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
                gridImageAdapter.upload(gridView);
            } else {
                Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
