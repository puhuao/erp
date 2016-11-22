package com.managesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by puhua on 2016/11/10.
 *
 * @
 */

public class Message implements Serializable{
    public static final String REGISTER_NOTICE = "10001";
    public static final String WORK_LIST_REMIND = "10002";
    public static final String MEETING_REMIND = "10003";
    public static final String MEETING_NOTICE = "10004";
    public static final String DESPATCH_NOTICE = "10005";
    public static final String WORK_LIST_NOTICE = "10006";
    //10001：注册提醒10002：工单提醒10003：会议提醒10004：会议通知10005：派单通知，10006：工单通知
    public String rid;
    public String type;
    public String content;
    public String ctime;
    public int messageId;
    public int status;
    public String title;
    public String userId;
    public Message(){}

}
