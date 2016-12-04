package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.AccountRecord;
import com.managesystem.model.Message;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 * 消费记录适配器
 */
public class AccountRecordAdapter extends BaseListAdapter<AccountRecord> {
    private int type = 1;//1：充值记录，2：消费记录

    public AccountRecordAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        AccountRecord message = mList.get(position);
        if (type ==1){
            holder.name.setText("充值成功");
        }else{
            holder.name.setText("消费成功");
        }
        holder.number.setText(String.valueOf(message.getMoney())+"元");
        holder.time.setText(String.valueOf(message.getCtime()));
        return convertView;
    }

    public void setType(int type) {
        this.type = type;
    }

    class ViewHolder{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.number)
        TextView number;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
