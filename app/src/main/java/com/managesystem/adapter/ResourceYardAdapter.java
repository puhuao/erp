package com.managesystem.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.managesystem.R;
import com.managesystem.model.ResourcePersonModel;
import com.managesystem.widegt.CustomDialog;
import com.wksc.framwork.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/29.
 */
public class ResourceYardAdapter extends BaseListAdapter<ResourcePersonModel> {
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

    public ResourceYardAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_resource_yard, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final ResourcePersonModel resourcePersonModel = mList.get(position);
        if (resourcePersonModel.getStatus()==1){
            holder.type.setText(resourcePersonModel.getMaterialName()+"("+"旧设备"+")");
        }else if (resourcePersonModel.getStatus()==0){
            holder.type.setText(resourcePersonModel.getMaterialName()+"("+"新设备"+")");
        }
        holder.name.setText(resourcePersonModel.getBrand());
        holder.name.setText("品牌:"+resourcePersonModel.getBrand());
        holder.checkBox.setChecked(resourcePersonModel.isCheck);


        if (resourcePersonModel.getModel().equals("null")||
                StringUtils.isBlank(resourcePersonModel.getModel())){
            holder.typeNumber.setText("型号:无");
        }else{
            holder.typeNumber.setText("型号:"+resourcePersonModel.getModel());
        }
        if (resourcePersonModel.getParam().equals("null")||
                StringUtils.isBlank(resourcePersonModel.getParam())){
            holder.param.setText("参数:无");
        }else{
            holder.param.setText("参数:"+resourcePersonModel.getParam());
        }
        if (!StringUtils.isBlank(resourcePersonModel.getSerialNumber())){
            if (resourcePersonModel.getSerialNumber().equals("null")){
                holder.serialNumber.setText("序列号:无");
            }else{
                holder.serialNumber.setText("序列号:"+resourcePersonModel.getSerialNumber());
            }
        }else{
            holder.serialNumber.setText("序列号:无");
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isFromCheckAll){
                    if (isChecked){
                        mList.get(position).isCheck = true;
                        if (StringUtils.isBlank(resourcePersonModel.getSerialNumber())){
                            if (isNeedSeareaNumber){
                                showDialog(holder,resourcePersonModel);
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
        @Bind(R.id.serialNumber)
        TextView serialNumber;
        @Bind(R.id.type_number)
        TextView typeNumber;
        @Bind(R.id.param)
        TextView param;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

    public void showDialog(final ViewHolder holder, final ResourcePersonModel resourcePersonModel){
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
                        notifyDataSetChanged();
                    }
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isFromCheckAll = true;
                    holder.checkBox.setChecked(false);
                    resourcePersonModel.isCheck = false;
                    isFromCheckAll = false;
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

}
