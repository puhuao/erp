package com.managesystem.fragment.resource;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.WriterException;
import com.managesystem.R;
import com.managesystem.activity.MainTainApplyActivity;
import com.managesystem.adapter.ResourcePersonAdapter;
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

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8.
 * 我的物资
 */
public class ResourcePersonalFragment extends BaseListRefreshFragment<ResourcePersonModel> {
    ResourcePersonAdapter resourcePersonAdapter;
    ArrayList<ResourcePersonModel> resourcePersonModels = new ArrayList<>();
    String userID;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_resource_person, null);
        ButterKnife.bind(this, container);
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        userID = config.getString("userId", "");
        initView();
        return container;
    }

    private void initView() {
        isfirstFragment = true;
        setHeaderTitle(getStringFromResource(R.string.resource_my));
        getTitleHeaderBar().setRightText(getStringFromResource(R.string.check_all));
        getTitleHeaderBar().getRightViewContainer().setVisibility(View.VISIBLE);
        resourcePersonAdapter = new ResourcePersonAdapter(getContext());
        setData(resourcePersonModels, resourcePersonAdapter);
        getTitleHeaderBar().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check_all
                resourcePersonAdapter.setIsFromCheckAll(true);
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    r.isCheck = true;
                }
                resourcePersonAdapter.notifyDataSetChanged();
                resourcePersonAdapter.setIsFromCheckAll(false);
            }
        });
    }

    @OnClick({R.id.send, R.id.fix})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                createQrCode(v,1);
                break;
            case R.id.fix:
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
                StringBuilder sb = new StringBuilder();
                for (ResourcePersonModel r :
                        resourcePersonModels) {
                    if (r.isCheck) {
                        sb.append(r.getMaterialName() + ",");
                    }
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                bundle.putString("string",sb.toString());
                startActivity(MainTainApplyActivity.class,bundle);
                break;
        }
    }

    private void createQrCode(View v,int type) {//创建交接二维码
        String ms = getStringParam();
        QrResourceModel qrResourceModel = new QrResourceModel();
        qrResourceModel.setType("3");
        QRresourceSend qRresourceSend = new QRresourceSend();
        StringBuilder sb = new StringBuilder("?");
        UrlUtils.getInstance(sb).praseToUrl("type", String.valueOf(type))//1：交接2：发放
                .praseToUrl("fromUserId", userID).removeLastWord();
        sb.append(ms);
        qRresourceSend.setPStr(sb.toString());
        qrResourceModel.setParam(qRresourceSend);
        try {
            Bitmap bitmap = CreateQrCode.createQRCode(GsonUtil.objectToJson(qrResourceModel), 300);
            QrcodeViewPopupwindow popupwindow = new QrcodeViewPopupwindow(getContext(), bitmap);
            popupwindow.showPopupwindow(v);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private String getStringParam() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ResourcePersonModel r :
                resourcePersonModels) {
            if (r.isCheck) {
                sb.append("&materials[" + i + "].materialId=" + r.getMaterialId());
                i++;
            }
        }
        return sb.toString();
    }


    @Override
    public void loadMore(int pageNo) {
        StringBuilder sb = new StringBuilder(Urls.RESOURCE_LIST);
        UrlUtils.getInstance(sb).praseToUrl("pageNo", String.valueOf(pageNo))
                .praseToUrl("userId", userID)
                .praseToUrl("pageSize", "20")
                .praseToUrl("keyword", "")
                .removeLastWord();
        excute(sb.toString(), ResourcePersonModel.class);
    }
}
