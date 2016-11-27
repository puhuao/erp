package com.managesystem.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class Maintain {

    /**
     * materialnameIds : null
     * materialNames : 电脑,笔记本
     * status : 3
     * handlerInfo : 电脑年代已久，已更换
     * star : 4
     * responsibleUserId : xiaoti
     * servicetypeName : 设备
     * ctime : 2016-11-21 17:33
     * departmentName : 测试
     * servicetypeId : 710d2a9f46d54573a416edebb8f18a43
     * content : 不错
     * infor : 用不了了
     * userId : xiaoti
     * name : mr
     * cphone : 22311452
     * orderId : fcfa93b6e6ae444c91a8de539ce1be7b
     */

    private String materialnameIds;
    private String materialNames;
    private int status;
    private String handlerInfo;
    private int star;
    private String responsibleUserId;
    private String servicetypeName;
    private String ctime;
    private String departmentName;
    private String servicetypeId;
    private String content;
    private String infor;
    private String userId;
    private String name;
    private String cphone;
    private String orderId;
    private List<Users> handleUsers;

    public List<Users> getHandleUsers() {
        return handleUsers;
    }

    public void setHandleUsers(List<Users> handleUsers) {
        this.handleUsers = handleUsers;
    }

    public String getMaterialnameIds() {
        return materialnameIds;
    }

    public void setMaterialnameIds(String materialnameIds) {
        this.materialnameIds = materialnameIds;
    }

    public String getMaterialNames() {
        return materialNames;
    }

    public void setMaterialNames(String materialNames) {
        this.materialNames = materialNames;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHandlerInfo() {
        return handlerInfo;
    }

    public void setHandlerInfo(String handlerInfo) {
        this.handlerInfo = handlerInfo;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getResponsibleUserId() {
        return responsibleUserId;
    }

    public void setResponsibleUserId(String responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public String getServicetypeName() {
        return servicetypeName;
    }

    public void setServicetypeName(String servicetypeName) {
        this.servicetypeName = servicetypeName;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getServicetypeId() {
        return servicetypeId;
    }

    public void setServicetypeId(String servicetypeId) {
        this.servicetypeId = servicetypeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
