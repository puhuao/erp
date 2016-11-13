package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/12.
 */
public class MeetingRoom {
    private String meetingroomId;
    private String meetingroomName;

    public String getMeetingroomName() {
        return meetingroomName;
    }

    public void setMeetingroomName(String meetingroomName) {
        this.meetingroomName = meetingroomName;
    }

    public String getMeetingroomId() {
        return meetingroomId;
    }

    public void setMeetingroomId(String meetingroomId) {
        this.meetingroomId = meetingroomId;
    }
}
