package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/17.
 */
public class WorkList {

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
    private Object star;
    private String servicetypeName;
    private String date;
    private String servicetypeId;
    private String distributeId;

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

    public Object getStar() {
        return star;
    }

    public void setStar(Object star) {
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
}
