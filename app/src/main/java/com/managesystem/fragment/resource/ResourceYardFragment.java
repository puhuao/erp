package com.managesystem.fragment.resource;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.adapter.ResourceYardAdapter;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.ResStatusSelectEvent;
import com.managesystem.event.ResYardTypeSelectEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.model.ResourceType;
import com.managesystem.model.Status;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.popupwindow.ResourceStatusSelectPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;
import com.wksc.framwork.zxing.qrcodeModel.QrResourceModel;

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
 *仓库管理
 */
public class ResourceYardFragment extends BaseListRefreshFragment<ResourcePersonModel> {
    @Bind(R.id.fab)
    Button fab;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.status)
    TextView tvStatus;
    @Bind(R.id.search)
    EditText search;
    @Bind(R.id.img_search)
    ImageView imgSearch;
    ResourceYardAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    private IConfig config;
    private String userID;
    private ArrayList<ResourceType> resourceTypes = new ArrayList<>();
    private ResourceType resourceType;
    private ArrayList<Status> statuses = new ArrayList<>();
    private Status status;
    private String keyword;
    private boolean checkAll = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_yard, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        status = new Status();
        status.status = -1;
        status.name = "全部";
        resourceType = new ResourceType();
        resourceType.setMaterialTypeName("全部");
        config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        setHeaderTitle(getStringFromResource(R.string.resource_yard));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
//        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        getTitleHeaderBar().getRightTextView().setTextColor(
                getContext().getResources().getColor(R.color.text_hint));
        resourcePersonAdapter = new ResourceYardAdapter(getContext());
        resourcePersonAdapter.setNeedSeareaNumber(true);
        setData(resourcePersonModels,resourcePersonAdapter);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
                checkAll = !checkAll;
                if (checkAll){
                    getTitleHeaderBar().getRightTextView().setTextColor(
                            getContext().getResources().getColor(R.color.title_bar_right));
                }else{
                    getTitleHeaderBar().getRightTextView().setTextColor(
                            getContext().getResources().getColor(R.color.text_hint));
                }
                resourcePersonAdapter.setIsFromCheckAll(true);
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    if (checkAll){
                        r.isCheck = true;
                    }else{
                        r.isCheck = false;
                    }
                }
                resourcePersonAdapter.notifyDataSetChanged();
                resourcePersonAdapter.setIsFromCheckAll(false);
            }
        });
    }
    private StringBuilder ids = new StringBuilder();//id
    private StringBuilder serialNumbers = new StringBuilder();//序列号
    private void getStringParam(){
        int i =0 ;
        if (ids.length()>0){
            ids.delete(0,ids.length());
            serialNumbers.delete(0,serialNumbers.length());
        }
        for (ResourcePersonModel r :
                resourcePersonModels) {
            if (r.isCheck){
                ids.append("&materials["+i+"].materialId="+r.getMaterialId());
                serialNumbers.append("&materials["+i+"].serialNumber="+r.getSerialNumber());
                i++;
            }
        }

    }

    @Override
    public void loadMore(int pageNo) {
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("pageSize","20")
                .praseToUrl("keyword",keyword)
                .praseToUrl("status",status.status>-1?String.valueOf(status.status):"")
                .praseToUrl("materialtypeId", StringUtils.isBlank(resourceType.getMaterialtypeId())?"":resourceType.getMaterialtypeId())
                .removeLastWord();
        excute(sb.toString(),ResourcePersonModel.class);
    }


    @OnClick({R.id.type, R.id.status, R.id.img_search, R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                getStringParam();
                if (ids.length()==0){
                    ToastUtil.showShortMessage(getContext(),"请选择交接的物资");
                    return;
                }
                QrResourceModel qrResourceModel = new QrResourceModel();
                qrResourceModel.setType("3");
                QRresourceSend qRresourceSend = new QRresourceSend();
                StringBuilder sb = new StringBuilder("?");
                UrlUtils.getInstance(sb).praseToUrl("type","2")//1：交接2：发放
                        .praseToUrl("fromUserId",userID).removeLastWord();
                sb.append(ids);
                sb.append(serialNumbers);
                qRresourceSend.setPStr(sb.toString());
                qrResourceModel.setParam(qRresourceSend);
                try {
                    Bitmap bitmap = CreateQrCode.createQRCode(GsonUtil.objectToJson(qrResourceModel), 300);
                    QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(),bitmap);
                    popupwindow.showPopupwindow(v);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.status:
                if (statuses.size()==0)
                    initStatus();
                ResourceStatusSelectPopupwindow popupwindow = new ResourceStatusSelectPopupwindow(getContext(),statuses);
                popupwindow.showPopupwindow(v);
                break;
            case R.id.type:
                if (resourceTypes.size() == 0) {
                    getResourceTyps(v);
                } else {
                    hideSoftInput(v);
                    getContext().pushFragmentToBackStack(ResourceYardTypeSelectFragment.class,resourceTypes);
                }
                break;
            case R.id.img_search:
                keyword = search.getText().toString();
                pageNo = 1;
                loadMore(1);
                break;
        }
    }

    private void initStatus() {
        Status statusAll = new Status();
        statusAll.name = "全部";
        statusAll.status = -1;
        statuses.add(statusAll);
        Status statusNew = new Status();
        statusNew.name = "新设备";
        statusNew.status = 0;
        statuses.add(statusNew);
        Status statusOld = new Status();
        statusOld.name = "旧设备";
        statusOld.status = 1;
        statuses.add(statusOld);
    }

    @Subscribe
    public void onEvent(ResYardTypeSelectEvent event) {
        resourceType = event.getResourceName();
        type.setText(event.getResourceName().getMaterialTypeName());
        pageNo=1;
        loadMore(1);

    }

    @Subscribe
    public void onEvent(ResStatusSelectEvent event){
        status = event.status;
        tvStatus.setText(event.status.name);
        pageNo=1;
        loadMore(1);
    }

    private void getResourceTyps(final View v) {
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_TYPE);
        UrlUtils.getInstance(sb);
        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(getContext(), "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    resourceTypes.addAll(GsonUtil.fromJsonList(o, ResourceType.class));
                    resourceTypes.add(0,resourceType);
                    hideSoftInput(v);
                    getContext().pushFragmentToBackStack(ResourceYardTypeSelectFragment.class,resourceTypes);
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
