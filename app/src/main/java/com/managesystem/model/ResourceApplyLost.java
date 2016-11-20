package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ResourceApplyLost {
    private String materialTypeName;
    private String utime;
    private String materialName;
    private String applyUserId;
    private String remark;
    private String status;//1:未审批，2：退回，3：已审批
    private String materialId;
    private String materialOrderId;
    private String picurl;
    private String handleName;
    private String type;
    private String handleUserId;

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialOrderId() {
        return materialOrderId;
    }

    public void setMaterialOrderId(String materialOrderId) {
        this.materialOrderId = materialOrderId;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHandleUserId() {
        return handleUserId;
    }

    public void setHandleUserId(String handleUserId) {
        this.handleUserId = handleUserId;
    }
}
