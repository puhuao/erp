package com.managesystem.fragment.resource;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.WriterException;
import com.managesystem.R;
import com.managesystem.adapter.ResourcePersonAdapter;
import com.managesystem.adapter.ResourceYardAdapter;
import com.managesystem.config.Urls;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;
import com.wksc.framwork.zxing.qrcodeModel.QrResourceModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 *仓库管理
 */
public class ResourceYardFragment extends BaseListRefreshFragment<ResourcePersonModel> {
    @Bind(R.id.fab)
    Button fab;
    ResourceYardAdapter resourcePersonAdapter;
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
        isfirstFragment = true;
        config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        setHeaderTitle(getStringFromResource(R.string.resource_yard));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        resourcePersonAdapter = new ResourceYardAdapter(getContext());
        resourcePersonAdapter.setNeedSeareaNumber(true);
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

                    getStringParam();
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
    private StringBuilder ids = new StringBuilder();//id
    private StringBuilder serialNumbers = new StringBuilder();//序列号
    private void getStringParam(){
        int i =0 ;
        if (ids.length()>0){
            ids.delete(0,ids.length()-1);
            serialNumbers.delete(0,serialNumbers.length()-1);
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
                .praseToUrl("keyword","")
                .removeLastWord();
        excute(sb.toString(),ResourcePersonModel.class);
    }
}
