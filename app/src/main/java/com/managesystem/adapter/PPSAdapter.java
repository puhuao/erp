package com.managesystem.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.activity.ImageActivity;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.model.PPSModel;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.EmojiTextView;
import com.managesystem.widegt.multiImageView.MultiImageView;
import com.wksc.framwork.BaseApplication;
import com.wksc.framwork.platform.config.IConfig;
import com.wksc.framwork.util.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

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
        final ViewHolder holder;
        convertView = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_pps, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final PPSModel ppsModel = mList.get(position);
        if (ppsModel.getPraise()){
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.img_zan_select);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            holder.zan.setCompoundDrawables(drawable, null, null, null);
            holder.zan.setFocusable(false);
        }else{
            holder.zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    praise(ppsModel,holder);
                }
            });
        }

        holder.zan.setText(String.valueOf(ppsModel.getPraiseCount()));
        holder.name.setText(ppsModel.getName());
        holder.time.setText(ppsModel.getCtime());
        holder.content.setText(ppsModel.getContent());
//        holder.comment.setText(String.valueOf(ppsModel.get));
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

    private void praise(final PPSModel ppsModel, final ViewHolder holder){
        IConfig config = BaseApplication.getInstance().getCurrentConfig();
        StringBuilder sb = new StringBuilder(Urls.PPS_ZAN);
        UrlUtils.getInstance(sb) .praseToUrl("topicId", ppsModel.getTopicId())
                .praseToUrl("userId", config.getString("userId", ""))
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(mContext, String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(mContext,"网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o!=null){
                    ToastUtil.showShortMessage(mContext,"点赞成功");
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.img_zan_select);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    holder.zan.setCompoundDrawables(drawable, null, null, null);
                    holder.zan.setFocusable(false);
                    ppsModel.setPraise(true);
                    holder.zan.setText(String.valueOf(ppsModel.getPraiseCount()+1));
                }
            }
        };
        callback.setDialogHide();
        OkHttpUtils.post(sb.toString())//
                .tag(this)//
                .execute(callback);
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
