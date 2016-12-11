package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.MeetingApplyRecord;
import com.managesystem.model.MeetingRoomDetail;
import com.wksc.framwork.util.StringUtils;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingApplyRecordAdapter extends BaseListAdapter<MeetingRoomDetail> {
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    public MeetingApplyRecordAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_meeting_apply_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        MeetingRoomDetail detail = mList.get(position);
        holder.location.setText("会议地点:"+detail.getArea());
        if (!StringUtils.isBlank(detail.getCtime())){

            holder.time.setText("时间:"+detail.getCtime().substring(0,16));
        }
        holder.name.setText(detail.getMeetingName());
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
                break;
            case 4:
                holder.tag.setText("已完成");
                break;
        }

        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.tag)
        TextView tag;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
