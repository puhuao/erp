package com.managesystem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class WorkList implements Serializable {

    /**
     * content : null
     * remark : 是
     * status : 2
     * rid : 4b4836df4df84301b45db42057b350ab
     * important : 紧急
     * userId : 51c7dd3b5926472db1db7b87bcaa4c6f
     * star : null
     * servicetypeName : 会议
     * date : 2016-11-15 14:58
     * servicetypeId : 2cb289f548e24c86b8381cbc28fa22ba
     * distributeId : 598e286195cd401f8842041261b04bda
     */

    private String content;
    private String remark;
    private int status;
    private String rid;
    private String important;
    private String userId;
    private String star;
    private String servicetypeName;
    private String date;
    private String servicetypeId;
    private String distributeId;
    private List<Users> handleUsers;
    private String name;
    private String departmentName;
    private String officeNo;

    public String getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = officeNo;
    }

    /**
     * materialnameIds : null
     * materialNames : 易耗,易耗
     * handlerInfo : null
     * responsibleUserId : 51c7dd3b5926472db1db7b87bcaa4c6f
     * ctime : 2016-11-27 12:18
     * infor : 看看
     * cphone : 22311452
     * orderId : e671bc9db8824de7a3be3089b8dcf523
     */

    private String materialnameIds;
    private String materialNames;
    private String handlerInfo;
    private String responsibleUserId;
    private String ctime;
    private String infor;
    private String cphone;
    private String orderId;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getHandleUsers() {
        return handleUsers;
    }

    public void setHandleUsers(List<Users> handleUsers) {
        this.handleUsers = handleUsers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getServicetypeName() {
        return servicetypeName;
    }

    public void setServicetypeName(String servicetypeName) {
        this.servicetypeName = servicetypeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServicetypeId() {
        return servicetypeId;
    }

    public void setServicetypeId(String servicetypeId) {
        this.servicetypeId = servicetypeId;
    }

    public String getDistributeId() {
        return distributeId;
    }

    public void setDistributeId(String distributeId) {
        this.distributeId = distributeId;
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

    public String getHandlerInfo() {
        return handlerInfo;
    }

    public void setHandlerInfo(String handlerInfo) {
        this.handlerInfo = handlerInfo;
    }

    public String getResponsibleUserId() {
        return responsibleUserId;
    }

    public void setResponsibleUserId(String responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
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
