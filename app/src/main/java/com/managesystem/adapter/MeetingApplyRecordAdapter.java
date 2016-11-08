package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.managesystem.R;
import com.managesystem.model.MeetingApplyRecord;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingApplyRecordAdapter extends BaseListAdapter<MeetingApplyRecord> {

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
        return convertView;
    }

    class ViewHolder{
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
