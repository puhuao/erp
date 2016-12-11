package com.managesystem.fragment.resource;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.managesystem.R;
import com.managesystem.adapter.PopResourceNameAdapter;
import com.managesystem.adapter.PopResourceTypeAdapter;
import com.managesystem.event.ResNameSelectEvent;
import com.managesystem.event.ResTypeSelectEvent;
import com.managesystem.model.ResourceName;
import com.managesystem.model.ResourceType;
import com.wksc.framwork.baseui.fragment.CommonFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 *物资申请类别选择
 */
public class ResourceNameSelectFragment extends CommonFragment {
    @Bind(R.id.list_view)
    ListView listView ;
    Activity mContext;
    PopResourceNameAdapter adapter;
    private List<ResourceName> list;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.pop_department_select, null);
        ButterKnife.bind(this, container);
        list = (List<ResourceName>)getmDataIn();
        initView();
        return container;
    }

    private void initView() {
        setHeaderTitle("选择名称");
        adapter = new PopResourceNameAdapter(getContext());
        listView.setAdapter(adapter);
        adapter.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResNameSelectEvent event = new ResNameSelectEvent(list.get(position));
                EventBus.getDefault().post(event);
                getContext().popTopFragment(null);
            }
        });
    }
}
