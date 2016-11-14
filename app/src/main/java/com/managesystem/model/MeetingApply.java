package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/12.
 */
public class MeetingApply {

    /**
     * servicetypeId : 2cb289f548e24c86b8381cbc28fa22ba
     * meetingroomId : 231212
     * meetingName : 送送送
     * userId : 0d8d4e5d43ba44b7a51c306b13a8554f
     * startDate : 2011-09-10%2010:00:00
     * endDate : 2011-09-10%2010:30:00
     */

//    private String servicetypeId;
    private String meetingroomId;
    private String meetingName;
    private String userId;
    private String startDate;
    private String endDate;
    private String infor;

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

//    public String getServicetypeId() {
//        return servicetypeId;
//    }
//
//    public void setServicetypeId(String servicetypeId) {
//        this.servicetypeId = servicetypeId;
//    }

    public String getMeetingroomId() {
        return meetingroomId;
    }

    public void setMeetingroomId(String meetingroomId) {
        this.meetingroomId = meetingroomId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
