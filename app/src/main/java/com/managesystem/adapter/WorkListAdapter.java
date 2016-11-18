package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.GoodNews;
import com.managesystem.model.WorkList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class WorkListAdapter extends BaseListAdapter<WorkList> {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;//0已完成1未完成

    public WorkListAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_work_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        WorkList workList = mList.get(position);
        holder.time.setText("时间:"+workList.getDate());
        holder.remark.setText("备注:"+workList.getRemark());
        if (type == 1){
            if (workList.getImportant().equals("紧急")){
                holder.urgent.setVisibility(View.VISIBLE);
            }else{
                holder.urgent.setVisibility(View.GONE);
            }
        }else{
            holder.urgent.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.remark)
        TextView remark;
        @Bind(R.id.urgent)
        ImageView urgent;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}