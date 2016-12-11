package com.managesystem.fragment.resource;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.zxing.WriterException;
import com.managesystem.R;
import com.managesystem.adapter.PopResourceTypeAdapter;
import com.managesystem.adapter.ResourceYardAdapter;
import com.managesystem.config.Urls;
import com.managesystem.event.ResTypeSelectEvent;
import com.managesystem.fragment.BaseListRefreshFragment;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.model.ResourceType;
import com.managesystem.popupwindow.QrcodeViewPopupwindow;
import com.managesystem.tools.UrlUtils;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.baseui.fragment.CommonFragment;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.GsonUtil;
import com.wksc.framwork.zxing.CreateQrCode;
import com.wksc.framwork.zxing.qrcodeModel.QRresourceSend;
import com.wksc.framwork.zxing.qrcodeModel.QrResourceModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 *物资申请类别选择
 */
public class ResourceTypeSelectFragment extends CommonFragment {
    @Bind(R.id.list_view)
    ListView listView ;
    Activity mContext;
    PopResourceTypeAdapter adapter;
    private List<ResourceType> list;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.pop_department_select, null);
        ButterKnife.bind(this, container);
        list = (List<ResourceType>)getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("选择类别");
        adapter = new PopResourceTypeAdapter(getContext());
        listView.setAdapter(adapter);
        adapter.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResTypeSelectEvent event = new ResTypeSelectEvent(list.get(position));
                EventBus.getDefault().post(event);
                getContext().popTopFragment(null);
            }
        });
    }
}
