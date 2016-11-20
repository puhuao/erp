package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.GoodNews;
import com.managesystem.model.ResourcePersonModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class ResourcePersonAdapter extends BaseListAdapter<ResourcePersonModel> {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();

    public ResourcePersonAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_resource_person, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ResourcePersonModel resourcePersonModel = mList.get(position);
        holder.type.setText("类型:"+resourcePersonModel.getMaterialtypeName());
        holder.name.setText("名称:"+resourcePersonModel.getMaterialName());

        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.name)
        TextView name;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
