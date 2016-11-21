package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private boolean isFromCheckAll = false;

    public ResourcePersonAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_resource_person, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ResourcePersonModel resourcePersonModel = mList.get(position);
        holder.type.setText(resourcePersonModel.getMaterialName());
        holder.name.setText(resourcePersonModel.getBrand());
        holder.checkBox.setChecked(resourcePersonModel.isCheck);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isFromCheckAll){
                    if (isChecked){
                        mList.get(position).isCheck = true;
                    }else{
                        mList.get(position).isCheck = false;
                    }
                }
            }
        });
        return convertView;
    }

    public void setIsFromCheckAll(boolean isFromCheckAll) {
        this.isFromCheckAll = isFromCheckAll;
    }

    class ViewHolder{
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.checkbox)
        CheckBox checkBox;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
