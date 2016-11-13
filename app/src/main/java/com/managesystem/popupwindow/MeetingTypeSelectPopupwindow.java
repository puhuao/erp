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
import com.managesystem.adapter.PopMeetingRoomAdapter;
import com.managesystem.adapter.PopMeetingTypeAdapter;
import com.managesystem.event.MeetingRoomSelectEvent;
import com.managesystem.event.MeetingTypeSelectEvent;
import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingType;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingTypeSelectPopupwindow extends PopupWindow {
    ListView listView ;
    Activity mContext;
    PopMeetingTypeAdapter adapter;
    private List<MeetingType> list;
    public MeetingTypeSelectPopupwindow(Activity context, final List<MeetingType> list){
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
        adapter = new PopMeetingTypeAdapter(context);
        listView.setAdapter(adapter);
        adapter.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dissmisPopupwindow();
                MeetingTypeSelectEvent event = new MeetingTypeSelectEvent(list.get(position));
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
