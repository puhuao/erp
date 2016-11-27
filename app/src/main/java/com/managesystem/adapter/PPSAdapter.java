package com.managesystem.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.activity.ImageActivity;
import com.managesystem.config.Urls;
import com.managesystem.model.PPSModel;
import com.managesystem.widegt.EmojiTextView;
import com.managesystem.widegt.multiImageView.MultiImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 * 论坛列表适配
 */
public class PPSAdapter extends BaseListAdapter<PPSModel> {

    public PPSAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_pps, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final PPSModel ppsModel = mList.get(position);
        holder.name.setText(ppsModel.getName());
        holder.time.setText(ppsModel.getCtime());
        holder.content.setText(ppsModel.getContent());
        final ArrayList<String> imgs = new ArrayList<>();
        if (ppsModel.getPics()!=null){

            for (String s :
                    ppsModel.getPics()) {
                imgs.add(Urls.GETPICS+s);
            }
            holder.multiImageView.setList(imgs);
        }
        holder.check.setText(String.valueOf(ppsModel.getScanCount()));
        holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                Bundle bd = null;
                if (bd == null) {
                    bd = new Bundle();
                }
                    bd.putStringArrayList("list", imgs);
                bd.putInt("position", position);
                intent.putExtras(bd);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_name)
        TextView name;
        @Bind(R.id.tv_time)
        TextView time;
        @Bind(R.id.tv_content)
        EmojiTextView content;
        @Bind(R.id.multi_imageView)
        MultiImageView multiImageView;
        @Bind(R.id.zan)
        TextView zan;
        @Bind(R.id.comment)
        TextView comment;
        @Bind(R.id.check)
        TextView check;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

}
