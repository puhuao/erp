package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.GoodNews;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class GoodNewsAdapter extends BaseListAdapter<GoodNews> {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();

    public GoodNewsAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_good_news, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        GoodNews goodNew = mList.get(position);
        holder.name.setText(goodNew.getTitle());
        holder.description.setText(goodNew.getInfor());
        if (goodNew.isIsApply()){
            holder.image.setVisibility(View.GONE);
        }else{
            holder.image.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.image)
        ImageView image;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
