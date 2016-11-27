package com.managesystem.model;

import java.util.List;

/**
 * Created by puhua on 2016/11/8.
 *
 * @
 */

public class MeetingAttendRecord {


    private String startDate;
    private String infor;
    private String area;
    private String meetingName;
    private String meetingId;
    private String endDate;
    private Object signUsers;
    private String meetingroomName;
    private Object noticeUsers;
    private int meetigStatus;//-1:未召开，0进行中，1：已召开
    /**
     * name : 9
     * userId : 3ea0396e43f84b82979df5fad733fd4a
     * type : 1
     */

    private List<Users> applyUsers;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getSignUsers() {
        return signUsers;
    }

    public void setSignUsers(Object signUsers) {
        this.signUsers = signUsers;
    }

    public String getMeetingroomName() {
        return meetingroomName;
    }

    public void setMeetingroomName(String meetingroomName) {
        this.meetingroomName = meetingroomName;
    }

    public Object getNoticeUsers() {
        return noticeUsers;
    }

    public void setNoticeUsers(Object noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public int getMeetigStatus() {
        return meetigStatus;
    }

    public void setMeetigStatus(int meetigStatus) {
        this.meetigStatus = meetigStatus;
    }

    public List<Users> getApplyUsers() {
        return applyUsers;
    }

    public void setApplyUsers(List<Users> applyUsers) {
        this.applyUsers = applyUsers;
    }

}
