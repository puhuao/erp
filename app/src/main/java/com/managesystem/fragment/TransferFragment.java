package com.managesystem.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.WriterException;
import com.managesystem.R;
import com.managesystem.adapter.ResourcePersonAdapter;
import com.managesystem.config.Urls;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.util.ToastUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;
import com.wksc.framwork.zxing.qrcodeModel.QrResourceModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 */
public class TransferFragment extends BaseListRefreshFragment<ResourcePersonModel> {
    @Bind(R.id.fab)
    Button fab;
    @Bind(R.id.ll_condition)
            View llCondition;
    ResourcePersonAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    private IConfig config;
    private String userID;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_yard, null);
        ButterKnife.bind(this, container);
        initView();
        return container;
    }

    private void initView() {
        enableDefaultBack(false);
        config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        setHeaderTitle(getStringFromResource(R.string.resource_transfer));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        llCondition.setVisibility(View.GONE);
        resourcePersonAdapter = new ResourcePersonAdapter(getContext());
        setData(resourcePersonModels,resourcePersonAdapter);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ms = getStringParam();
                if (ms.length()==0){
                    ToastUtil.showShortMessage(getContext(),"请选择交接的物资");
                    return;
                }
                QrResourceModel qrResourceModel = new QrResourceModel();
                qrResourceModel.setType("3");
                QRresourceSend qRresourceSend = new QRresourceSend();
                StringBuilder sb = new StringBuilder("?");
                UrlUtils.getInstance(sb).praseToUrl("type","1")//1：交接2：发放
                        .praseToUrl("fromUserId",userID).removeLastWord();
                sb.append(ms);
                qRresourceSend.setPStr(sb.toString());
                qrResourceModel.setParam(qRresourceSend);
                try {
                    Bitmap bitmap = CreateQrCode.createQRCode(GsonUtil.objectToJson(qrResourceModel), 300);
                    QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(),bitmap);
                    popupwindow.showPopupwindow(v);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
                resourcePersonAdapter.setIsFromCheckAll(true);
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    r.isCheck = !r.isCheck;
                }
                resourcePersonAdapter.notifyDataSetChanged();
                resourcePersonAdapter.setIsFromCheckAll(false);
            }
        });
    }


//    private void getResource(){
////        IConfig config = BaseApplication.getInstance().getCurrentConfig();
////        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
////        UrlUtils.getInstance(sb).praseToUrl("pageNo","1")
////                .praseToUrl("pageSize","20")
////                .praseToUrl("keyword","")
////                .removeLastWord();
//        DialogCallback callback = new DialogCallback<String>(getContext(), String.class) {
//            @Override
//            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                super.onError(isFromCache, call, response, e);
//                ToastUtil.showShortMessage(getContext(),"网络错误");
//            }
//            @Override
//            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
//                if (o!=null){
//                    try {
//                        JSONObject jsonObject = new JSONObject(o);
//                        String list = jsonObject.getString("list");
//                        resourcePersonModels.addAll(GsonUtil.fromJsonList(list, ResourcePersonModel.class));
//                        resourcePersonAdapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        };
//        OkHttpUtils.get(sb.toString())//
//                .tag(this)//
//                .execute(callback);
//    }

    private String getStringParam(){
        StringBuilder sb = new StringBuilder();
        int i =0 ;
        for (ResourcePersonModel r :
                resourcePersonModels) {
            if (r.isCheck){
                sb.append("&materials["+i+"].materialId="+r.getMaterialId());
                i++;
            }
        }
        return  sb.toString();
    }

    @Override
    public void loadMore(int pageNo) {
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo",String.valueOf(pageNo))
                .praseToUrl("userId", userID)
                .praseToUrl("pageSize","20")
                .praseToUrl("keyword","")
                .removeLastWord();
        excute(sb.toString(),ResourcePersonModel.class);
    }
}
