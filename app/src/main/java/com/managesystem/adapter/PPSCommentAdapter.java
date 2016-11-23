package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.config.Urls;
import com.managesystem.model.PPSComment;
import com.managesystem.model.PPSModel;
import com.managesystem.widegt.EmojiTextView;
import com.managesystem.widegt.multiImageView.MultiImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 * 论坛回复列表适配
 */
public class PPSCommentAdapter extends BaseListAdapter<PPSComment> {

    public PPSCommentAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_pps_comment, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        PPSComment ppsModel = mList.get(position);
        holder.name.setText(ppsModel.getName());
        holder.time.setText(ppsModel.getCtime());
        holder.content.setText(ppsModel.getContent());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_name)
        TextView name;
        @Bind(R.id.tv_time)
        TextView time;
        @Bind(R.id.tv_content)
        EmojiTextView content;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

}
