package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.Message;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 * 消息已读适配器
 */
public class MsgReadAdapter extends BaseListAdapter<Message> {

    public MsgReadAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_msg_read, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Message message = mList.get(position);
        holder.msgName.setText(message.content);
        if(message.status==1){
            holder.notice.setVisibility(View.GONE);
        }else{
            holder.notice.setVisibility(View.VISIBLE);
        }
        holder.time.setText(message.ctime);
        if (message.type.equals(Message.MEETING_REMIND)){
            holder.image.setImageResource(R.drawable.img_meeting_notice);

        }else if(message.type.equals(Message.REGISTER_NOTICE)){
            holder.image.setImageResource(R.drawable.img_user_register);
        }else if(message.type.equals(Message.WORK_LIST_NOTICE)){
            holder.image.setImageResource(R.drawable.img_fix_mession);

        }else if(message.type.equals(Message.MEETING_NOTICE)){
            holder.image.setImageResource(R.drawable.img_meeting_notice);

        }else if(message.type.equals(Message.DESPATCH_NOTICE)){
            holder.image.setImageResource(R.drawable.img_fix_mession);
        }else if(message.type.equals(Message.WORK_LIST_REMIND)){
            holder.image.setImageResource(R.drawable.img_fix_mession);
        }
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.msg_name)
        TextView msgName;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.img_notice)
        ImageView notice;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
