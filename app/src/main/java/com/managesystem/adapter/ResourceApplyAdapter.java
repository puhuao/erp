package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.GoodNews;
import com.managesystem.model.ResourceApplyLost;
import com.managesystem.model.ResourcePersonModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class ResourceApplyAdapter extends BaseListAdapter<ResourceApplyLost> {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();

    public ResourceApplyAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_resource_appluy, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ResourceApplyLost resourceApplyLost = mList.get(position);

        holder.name.setText(resourceApplyLost.getMaterialName());
        holder.time.setText(resourceApplyLost.getUtime());
        holder.manager.setText(resourceApplyLost.getHandleName());
        if (resourceApplyLost.getStatus().equals("1")){
            holder.status.setVisibility(View.VISIBLE);
            holder.iamge.setVisibility(View.GONE);
        }else if (resourceApplyLost.getStatus().equals("2")){
            holder.status.setVisibility(View.GONE);
            holder.iamge.setVisibility(View.VISIBLE);
            holder.iamge.setImageResource(R.drawable.img_delay);
        }else if (resourceApplyLost.getStatus().equals("3")){
            holder.status.setVisibility(View.GONE);
            holder.iamge.setVisibility(View.VISIBLE);
            holder.iamge.setImageResource(R.drawable.img_approval);
        }
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.manager)
        TextView manager;
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.image)
        ImageView iamge;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
