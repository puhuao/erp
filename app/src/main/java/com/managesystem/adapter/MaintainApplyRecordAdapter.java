package com.managesystem.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.Maintain;
import com.managesystem.model.MeetingRoomDetail;
import com.wksc.framwork.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MaintainApplyRecordAdapter extends BaseListAdapter<Maintain> {
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    public MaintainApplyRecordAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_maintain_apply_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Maintain detail = mList.get(position);
        if (!StringUtils.isBlank(detail.getCtime())){
            holder.time.setText("时间:"+detail.getCtime().substring(0,16));
        }
        holder.name.setText(detail.getServicetypeName());
        holder.content.setText(detail.getInfor());
        switch (detail.getStatus()){
            case 0:
                holder.tag.setText("派单中");
                break;
            case 1:
                holder.tag.setText("已派单");
                break;
            case 2:
                holder.tag.setText("处理中");
                break;
            case 3:
                holder.tag.setText("未评价");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tag.setBackground(mContext.getDrawable(R.drawable.shape_bacgroud_refuse));
                }else{
                    holder.tag.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bacgroud_refuse));
                }
//                holder.tag.setTextColor(mContext.getResources().getColor(R.color.refuse));
                break;
            case 4:
                holder.tag.setText("已完成");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tag.setBackground(mContext.getDrawable(R.drawable.shape_bacgroud_not_valify));
                }else{
                    holder.tag.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bacgroud_not_valify));
                }
                holder.tag.setTextColor(mContext.getResources().getColor(R.color.text_hint));
                break;
        }

        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.tag)
        TextView tag;
        @Bind(R.id.content)
        TextView content;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
