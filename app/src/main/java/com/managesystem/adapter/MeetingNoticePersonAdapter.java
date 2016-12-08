package com.managesystem.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.MeetingRoomDetail;
import com.managesystem.model.Users;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MeetingNoticePersonAdapter extends BaseListAdapter<Users> {
private int type;
    public MeetingNoticePersonAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_check, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Users department = mList.get(position);
        holder.name.setText(department.getName());
        if (type == 2){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            mList.get(position).isCheck = true;
                        }else{
                            mList.get(position).isCheck = false;
                        }
                }
            });
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.name.setBackground(mContext.getResources().getDrawable(R.drawable.selector_notice_select));
        }
        return convertView;
    }

    public void setType(int type) {
        this.type = type;
    }

    class ViewHolder{
        @Bind(R.id.tv_category_name)
        TextView name;
        @Bind(R.id.checkbox)
        CheckBox checkBox;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
