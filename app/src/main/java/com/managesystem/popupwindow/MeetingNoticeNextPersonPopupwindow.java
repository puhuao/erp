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
import com.managesystem.adapter.MeetingNoticePersonAdapter;
import com.managesystem.adapter.PopMeetingSignPersonAdapter;
import com.managesystem.model.Users;

import java.util.List;


/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingNoticeNextPersonPopupwindow extends PopupWindow {
    ListView listView ;
    Activity mContext;
    MeetingNoticePersonAdapter adapter;
    private List<Users> list;

    public MeetingNoticeNextPersonPopupwindow(Activity context, final List<Users> list){
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
        adapter = new MeetingNoticePersonAdapter(context);
        listView.setAdapter(adapter);
        adapter.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dissmisPopupwindow();
//                MeetingRoomSelectEvent event = new MeetingRoomSelectEvent(list.get(position));
//                EventBus.getDefault().post(event);
            }
        });
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
