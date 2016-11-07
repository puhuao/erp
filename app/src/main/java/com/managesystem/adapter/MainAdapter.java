package com.managesystem.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.managesystem.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class MainAdapter extends BaseListAdapter<String> {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();

    public MainAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_main, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        return convertView;
    }

    class ViewHolder{
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
