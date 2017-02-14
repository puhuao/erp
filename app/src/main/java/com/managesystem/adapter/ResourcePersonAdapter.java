package com.managesystem.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.managesystem.R;
import com.managesystem.callBack.DialogCallback;
import com.managesystem.config.Urls;
import com.managesystem.event.PPSListUpdateEvent;
import com.managesystem.event.PersonalResourceUpdate;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.tools.UrlUtils;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/5/29.
 */
public class ResourcePersonAdapter extends BaseListAdapter<ResourcePersonModel> {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();
    private boolean isFromCheckAll = false;
    private Boolean isNeedSeareaNumber = false;

    public Boolean getNeedSeareaNumber() {
        return isNeedSeareaNumber;
    }

    public void setNeedSeareaNumber(Boolean needSeareaNumber) {
        isNeedSeareaNumber = needSeareaNumber;
    }

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
        final ResourcePersonModel resourcePersonModel = mList.get(position);
        holder.type.setText(resourcePersonModel.getMaterialName());
        holder.name.setText(resourcePersonModel.getBrand());
        holder.checkBox.setChecked(resourcePersonModel.isCheck);
        holder.typeNumber.setText("型号:"+resourcePersonModel.getModel());
        holder.param.setText("参数:"+resourcePersonModel.getParam());
        if (!StringUtils.isBlank(resourcePersonModel.getSerialNumber())){
            if (resourcePersonModel.getSerialNumber().equals("null")){
                holder.serialNumber.setText("序列号:无");
            }else{
                holder.serialNumber.setText("序列号:"+resourcePersonModel.getSerialNumber());
            }
        }else{
            holder.serialNumber.setText("序列号:无");
        }

        if (isMyResource==1)
        if (resourcePersonModel.getStatus()==7){
            holder.ivRecord.setVisibility(View.VISIBLE);
        }
        if(resourcePersonModel.getStatus()==6){
            holder.ivRecord.setVisibility(View.VISIBLE);
            holder.typeLabel.setTextColor(mContext.getResources().getColor(R.color.text_hint));
            holder.brandLabel.setTextColor(mContext.getResources().getColor(R.color.text_hint));
            holder.type.setTextColor(mContext.getResources().getColor(R.color.text_hint));
            holder.serialNumber.setTextColor(mContext.getResources().getColor(R.color.text_hint));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.text_hint));
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showComfirmDialog(resourcePersonModel);
                }
            });
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isFromCheckAll){
                    if (isChecked){
                        mList.get(position).isCheck = true;
                        if (StringUtils.isBlank(resourcePersonModel.getSerialNumber())){
                            if (isNeedSeareaNumber){
                                showDialog(resourcePersonModel);
                            }
                        }
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
private int isMyResource;
    public void setIsMyResource(int isMyResource) {
        this.isMyResource = isMyResource;
    }

    class ViewHolder{
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.checkbox)
        CheckBox checkBox;
        @Bind(R.id.iv_record)
        ImageView ivRecord;
        @Bind(R.id.serial_number)
        TextView serialNumber;
        @Bind(R.id.container)
        LinearLayout container;
        @Bind(R.id.type_label)
        TextView typeLabel;
        @Bind(R.id.brand_label)
        TextView brandLabel;
        @Bind(R.id.type_number)
        TextView typeNumber;
        @Bind(R.id.param)
        TextView param;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

    public void showComfirmDialog(final ResourcePersonModel resourcePersonModel){
        final CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        if (resourcePersonModel.getIsNeedSerial()==1){
            builder.setTitle("确认补录");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    comfirm(resourcePersonModel.getMaterialId());
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public void showDialog(final ResourcePersonModel resourcePersonModel){
        final CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        if (resourcePersonModel.getIsNeedSerial()==1){
            builder.setTitle("请输入序列号");
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_text,null);
            builder.setContentView(view);
            final EditText editText = (EditText) view.findViewById(R.id.edit_text);
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!StringUtils.isBlank(editText.getText().toString())){
                        resourcePersonModel.setSerialNumber(editText.getText().toString());
                        dialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private void comfirm(String marterialId){
        StringBuilder sb = new StringBuilder(Urls.MATERIALOK);
        UrlUtils.getInstance(sb).praseToUrl("marterialId", marterialId)
                .removeLastWord();
        DialogCallback callback = new DialogCallback<String>(mContext, String.class) {
            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                ToastUtil.showShortMessage(mContext, "网络错误");
            }

            @Override
            public void onResponse(boolean isFromCache, String o, Request request, @Nullable Response response) {
                if (o != null) {
                    mList.clear();
                    notifyDataSetChanged();
                    ToastUtil.showShortMessage(mContext, "物资补录确认成功");
                    EventBus.getDefault().post(new PersonalResourceUpdate());
                }
            }
        };
        OkHttpUtils.get(sb.toString())//
                .tag(this)//
                .execute(callback);
    }

}
