package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.MeetingAttendRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingAttendRecordAdapter extends BaseListAdapter<MeetingAttendRecord> {

    public MeetingAttendRecordAdapter(Activity context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_meeting_attend_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        MeetingAttendRecord meetingAttendRecord = mList.get(position);
        holder.name.setText(meetingAttendRecord.getMeetingName());
        holder.location.setText(meetingAttendRecord.getArea());
        holder.time.setText(meetingAttendRecord.getStartDate());
        String sStatus = null;
        switch (meetingAttendRecord.getMeetigStatus()){
            case -1:
                sStatus = "未召开";
                break;
            case 0:
                sStatus = "进行中";
                if (showNotStart)
                    holder.layout.setVisibility(View.GONE);
                break;
            case 1:
                sStatus = "已召开";
                if (showNotStart)
                    holder.layout.setVisibility(View.GONE);
                break;
        }
        holder.status.setText(sStatus);
        return convertView;
    }
Boolean showNotStart = false;
    public void showNotStart() {
        showNotStart = true;
    }

    class ViewHolder{
        @Bind(R.id.layout)
        LinearLayout layout;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.status)
        TextView status;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
