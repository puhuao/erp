package com.managesystem.adapter;

import android.app.Activity;
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

    public HorizontalListViewAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_horizontal_calender, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        String weekDay = "";
        switch (position%7){
            case 1:
                weekDay = "一";
                break;
            case 2:
                weekDay = "二";
                break;
            case 3:
                weekDay = "三";
                break;
            case 4:
                weekDay = "四";
                break;
            case 5:
                weekDay = "五";
                break;
            case 6:
                weekDay = "六";
                break;
            case 0:
                weekDay = "日";
                break;
        }
        holder.tvWeekDay.setText(weekDay);
        holder.tvCalendarDay.setText(String.valueOf(position));
        return convertView;
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
