package com.managesystem.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

/**
 * Created by Administrator on 2016/11/12.
 */
public class MeetingSelectCondition implements Parcelable{
    private String pageNo = "1";
    private String pageSize = "20";
    private String userId;//传了userId,则查询我的申请记录。不传：则查询会议室使用一览表
    private String isQueryDetail;//默认0,不查询详情1：查询详情
    private String meetingName;
    private String date;
    private String meetingId;
    public MeetingSelectCondition(){

    }

    protected MeetingSelectCondition(Parcel in) {
        pageNo = in.readString();
        pageSize = in.readString();
        userId = in.readString();
        isQueryDetail = in.readString();
        meetingName = in.readString();
        date = in.readString();
        meetingId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pageNo);
        dest.writeString(pageSize);
        dest.writeString(userId);
        dest.writeString(isQueryDetail);
        dest.writeString(meetingName);
        dest.writeString(date);
        dest.writeString(meetingId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeetingSelectCondition> CREATOR = new Creator<MeetingSelectCondition>() {
        @Override
        public MeetingSelectCondition createFromParcel(Parcel in) {
            return new MeetingSelectCondition(in);
        }

        @Override
        public MeetingSelectCondition[] newArray(int size) {
            return new MeetingSelectCondition[size];
        }
    };

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsQueryDetail() {
        return isQueryDetail;
    }

    public void setIsQueryDetail(String isQueryDetail) {
        this.isQueryDetail = isQueryDetail;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
