package com.managesystem.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.HorizontalCalenderModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class HorizontalListViewAdapter extends BaseListAdapter<HorizontalCalenderModel> {

    public int currentPositon=0;
    private float x;

    public HorizontalListViewAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_horizontal_calender, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        HorizontalCalenderModel model = mList.get(position);
        String weekDay = "";
        switch (model.weekDay){
            case 2:
                weekDay = "一";
                break;
            case 3:
                weekDay = "二";
                break;
            case 4:
                weekDay = "三";
                break;
            case 5:
                weekDay = "四";
                break;
            case 6:
                weekDay = "五";
                break;
            case 7:
                weekDay = "六";
                break;
            case 1:
                weekDay = "日";
                break;
        }
        holder.tvWeekDay.setText(weekDay);
        holder.tvCalendarDay.setText(String.valueOf(model.name));
        if (currentPositon == position){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvCalendarDay.setBackground(mContext.getResources().getDrawable(R.drawable.shape_tody_bacgroud));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvCalendarDay.setBackground(mContext.getResources().getDrawable(R.drawable.shape_tody_bacgroud_normal));
            }
        }
        holder.tvWeekDay.setWidth((int) x);
        holder.tvCalendarDay.setWidth((int) x);
        holder.tvCalendarDay.setHeight((int) x);
        holder.tvWeekDay.setHeight((int) x);
        return convertView;
    }

    public void setX(float x) {
        this.x = x;
    }

    class ViewHolder{
        @Bind(R.id.week_day)
        TextView tvWeekDay;
        @Bind(R.id.calendar_day)
        TextView tvCalendarDay;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
