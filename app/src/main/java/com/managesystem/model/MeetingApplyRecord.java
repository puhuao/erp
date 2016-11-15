package com.managesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by puhua on 2016/11/8.
 *
 * @
 */

public class MeetingApplyRecord implements Parcelable {
    private String startDate;

    public MeetingApplyRecord(){

    }
    protected MeetingApplyRecord(Parcel in) {
        startDate = in.readString();
        status = in.readInt();
        endDate = in.readString();
        meetingroomName = in.readString();
        ctime = in.readString();
        departmentName = in.readString();
        servicetypeId = in.readString();
        infor = in.readString();
        area = in.readString();
        floor = in.readString();
        meetingName = in.readString();
        userId = in.readString();
        name = in.readString();
        meetingId = in.readString();
        cphone = in.readString();
        meetingroomId = in.readString();
        officeNo = in.readString();
    }

    public static final Creator<MeetingApplyRecord> CREATOR = new Creator<MeetingApplyRecord>() {
        @Override
        public MeetingApplyRecord createFromParcel(Parcel in) {
            return new MeetingApplyRecord(in);
        }

        @Override
        public MeetingApplyRecord[] newArray(int size) {
            return new MeetingApplyRecord[size];
        }
    };

    public ArrayList<Users> getNoticeUsers() {
        return noticeUsers;
    }

    public void setNoticeUsers(ArrayList<Users> noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public void setApplyUsers(ArrayList<Users> applyUsers) {
        this.applyUsers = applyUsers;
    }

    private int status;//0：新增 1：已派单2：已确认3：已完成4：已评价
    private String endDate;

    public ArrayList<Users> getSignUsers() {
        return signUsers;
    }

    public void setSignUsers(ArrayList<Users> signUsers) {
        this.signUsers = signUsers;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    private ArrayList<Users> handleUsers;
    private String meetingroomName;

    public ArrayList<Users> getHandleUsers() {
        return handleUsers;
    }

    public void setHandleUsers(ArrayList<Users> handleUsers) {
        this.handleUsers = handleUsers;
    }

    private String ctime;
    private String departmentName;
    private String servicetypeId;
    private ArrayList<Users> applyUsers;

    public ArrayList<Users> getApplyUsers() {
        return applyUsers;
    }

    private String infor;
    private String area;
    private String floor;
    private String meetingName;
    private String userId;
    private String name;
    private String meetingId;
    private ArrayList<Users> signUsers;
    private String cphone;
    private ArrayList<Users> noticeUsers;
    private String meetingroomId;
    private String officeNo;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMeetingroomName() {
        return meetingroomName;
    }

    public void setMeetingroomName(String meetingroomName) {
        this.meetingroomName = meetingroomName;
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingroomId() {
        return meetingroomId;
    }

    public void setMeetingroomId(String meetingroomId) {
        this.meetingroomId = meetingroomId;
    }

    public String getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = officeNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeInt(status);
        dest.writeString(endDate);
        dest.writeString(meetingroomName);
        dest.writeString(ctime);
        dest.writeString(departmentName);
        dest.writeString(servicetypeId);
        dest.writeString(infor);
        dest.writeString(area);
        dest.writeString(floor);
        dest.writeString(meetingName);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(meetingId);
        dest.writeString(cphone);
        dest.writeString(meetingroomId);
        dest.writeString(officeNo);
    }
}
