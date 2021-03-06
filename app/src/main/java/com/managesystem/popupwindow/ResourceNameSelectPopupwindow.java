package com.managesystem.popupwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.managesystem.R;
import com.managesystem.adapter.PopDepartmentAdapter;
import com.managesystem.adapter.PopResourceNameAdapter;
import com.managesystem.event.DepartmentSelectEvent;
import com.managesystem.event.ResNameSelectEvent;
import com.managesystem.model.Department;
import com.managesystem.model.ResourceName;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by Administrator on 2016/5/29.
 */
public class ResourceNameSelectPopupwindow extends PopupWindow {
    ListView listView ;
    Activity mContext;
    PopResourceNameAdapter adapter;
    private List<ResourceName> list;
    public ResourceNameSelectPopupwindow(Activity context, final List<ResourceName> list){
        super();
        mContext = context;
        this.list = list;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_department_select,null);
        listView = (ListView) view.findViewById(R.id.list_view);
        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                dismiss();
            }
        });
        adapter = new PopResourceNameAdapter(context);
        listView.setAdapter(adapter);
        adapter.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dissmisPopupwindow();
                ResNameSelectEvent event = new ResNameSelectEvent(list.get(position));
                EventBus.getDefault().post(event);
            }
        });
    }
    public void showPopupwindow(View view){
        backgroundAlpha(0.5f);
        showAtLocation(view, Gravity.CENTER,0,0);
        mContext.overridePendingTransition(com.wksc.framwork.R.anim.push_left_in,
                com.wksc.framwork.R.anim.push_left_out);
    }
    public void dissmisPopupwindow(){
        this.dismiss();
        mContext.overridePendingTransition(com.wksc.framwork.R.anim.push_left_in,
                com.wksc.framwork.R.anim.push_left_out);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0

        mContext.getWindow().setAttributes(lp);
    }

}
