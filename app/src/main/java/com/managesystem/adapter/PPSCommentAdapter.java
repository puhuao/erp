package com.managesystem.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.managesystem.R;
import com.managesystem.config.Urls;
import com.managesystem.model.PPSComment;
import com.managesystem.model.PPSModel;
import com.managesystem.widegt.EmojiTextView;
import com.managesystem.widegt.multiImageView.MultiImageView;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.widget.CircleImageView;

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
        final ViewHolder holder;

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
        String pic = null;
        if (!StringUtils.isBlank(ppsModel.getHeadPic())){
            pic = Urls.GETPICS+ppsModel.getHeadPic();
        }
        Glide.with(mContext).load(pic)
                .asBitmap().centerCrop()
                .error(R.drawable.ic_header_defalt)
                .placeholder(R.drawable.ic_header_defalt)
                .into(new BitmapImageViewTarget(holder.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.image.setImageDrawable(circularBitmapDrawable);
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
        @Bind(R.id.cirimg_user)
        CircleImageView image;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

}
