package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.Department;
import com.managesystem.model.ResourceName;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class PopResourceNameAdapter extends BaseListAdapter<ResourceName> {

    public PopResourceNameAdapter(Activity context) {
        super(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_main, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ResourceName department = mList.get(position);
        holder.name.setText(department.getMaterialName());
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.tv_category_name)
        TextView name;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
