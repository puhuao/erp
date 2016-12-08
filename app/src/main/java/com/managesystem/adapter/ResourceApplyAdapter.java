package com.managesystem.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.GoodNews;
import com.managesystem.model.ResourceApplyLost;
import com.managesystem.model.ResourcePersonModel;
import com.wksc.framwork.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class ResourceApplyAdapter extends BaseListAdapter<ResourceApplyLost> {
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

        holder.name.setText(resourceApplyLost.getMaterialTypeName()+"/"+resourceApplyLost.getMaterialName());
        holder.time.setText("领取时间:"+resourceApplyLost.getUtime());
        if (!StringUtils.isBlank(resourceApplyLost.getHandleName()))
        holder.manager.setText("管理员:"+resourceApplyLost.getHandleName());
        else{
            holder.manager.setVisibility(View.GONE);
        }
        if (resourceApplyLost.getStatus().equals("1")){
            holder.status.setVisibility(View.VISIBLE);
            holder.iamge.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.status.setTextAppearance(R.style.style_not_verify);
            }else{
                holder.status.setTextAppearance(mContext,R.style.style_not_verify);
            }
            holder.status.setText("未审批");
        }else if (resourceApplyLost.getStatus().equals("2")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.status.setTextAppearance(R.style.style_refuse);
            }else{
                holder.status.setTextAppearance(mContext,R.style.style_refuse);
            }
            holder.status.setText("未通过");
        }else if (resourceApplyLost.getStatus().equals("3")){
//            holder.status.setVisibility(View.GONE);
//            holder.iamge.setVisibility(View.VISIBLE);
//            holder.iamge.setImageResource(R.drawable.img_approval);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.status.setTextAppearance(R.style.style_verify);
            }else{
                holder.status.setTextAppearance(mContext,R.style.style_verify);
            }
            holder.status.setText("已审批");
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
