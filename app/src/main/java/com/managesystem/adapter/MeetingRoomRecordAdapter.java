package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.MeetingRoomDetail;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingRoomRecordAdapter extends BaseListAdapter<MeetingRoomDetail> {

    public MeetingRoomRecordAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_meeting_room_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        MeetingRoomDetail detail = mList.get(position);
        holder.location.setText(detail.getMeetingroomName());
        holder.time.setText(detail.getStartDate().substring(0,16)+"~"+detail.getEndDate().substring(0,16));
        holder.name.setText(detail.getMeetingName());
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.location)
        TextView location;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
