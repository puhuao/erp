package com.managesystem.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.HorizontalCalenderModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class HorizontalListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public int currentPositon=0;
    private float x;
    public ArrayList<HorizontalCalenderModel> mList = new ArrayList<>();

    public void setmList(ArrayList<HorizontalCalenderModel> mList) {
        this.mList = mList;
    }

    private Context mContext;
    public HorizontalListViewAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_horizontal_calender, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
        ((ViewHolder)holder).tvWeekDay.setText(weekDay);
        ((ViewHolder)holder).tvCalendarDay.setText(String.valueOf(model.name));
        if (currentPositon == position){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ((ViewHolder)holder).tvCalendarDay.setBackground(mContext.getResources().getDrawable(R.drawable.shape_tody_bacgroud));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ((ViewHolder)holder).tvCalendarDay.setBackground(mContext.getResources().getDrawable(R.drawable.shape_tody_bacgroud_normal));
            }
        }
        ((ViewHolder)holder).llItem.setLayoutParams(new RecyclerView.LayoutParams((int) x, ViewGroup.LayoutParams.WRAP_CONTENT));
//        ((ViewHolder)holder).tvCalendarDay.setWidth((int) x);
//        ((ViewHolder)holder).tvCalendarDay.setHeight((int) x);
//        ((ViewHolder)holder).tvWeekDay.setHeight((int) x);
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    public void setX(float x) {
        this.x = x;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.week_day)
        TextView tvWeekDay;
        @Bind(R.id.calendar_day)
        TextView tvCalendarDay;
        @Bind(R.id.item)
        View llItem;
        public ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this,convertView);
        }
    }

}
