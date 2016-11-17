package com.managesystem.jpush;

/**
 * Created by Administrator on 2016/11/17.
 */
public class JPUSHModel {
    public static final String REGISTER_NOTICE = "10001";
    public static final String WORKLIST_NOTICE = "10002";
    public static final String MEETING_REMIND = "10003";
    public static final String MEETING_NOTICE = "10004";
    public static final String DESPATCH_NOTICE = "10005";
    //10001：注册提醒10002：工单提醒10003：会议提醒10004：会议通知10005：派单通知
    public String rid;
    public String type;
    public String content;
}
