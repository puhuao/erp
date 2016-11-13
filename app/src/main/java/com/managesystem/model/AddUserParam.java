package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/13.
 */
public class AddUserParam {
   private String meetingId;
    private String[] userIds;
    private String type;//0:通知1:报名2:签到
    private String isAll;//默认00:不通知所有1：通知所有人

    public AddUserParam(String meetingId, String type, String isAll) {
        this.meetingId = meetingId;
        this.type = type;
        this.isAll = isAll;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsAll() {
        return isAll;
    }

    public void setIsAll(String isAll) {
        this.isAll = isAll;
    }
}
