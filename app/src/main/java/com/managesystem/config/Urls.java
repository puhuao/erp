package com.managesystem.config;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Urls {
    public static final String BASE_URL = "http://101.200.172.229:8081/";
    public static final String LOGIN = BASE_URL+"user/login?";//登录
    public static final String GET_VALIDCODE = BASE_URL+"getCode?";//获取验证码
    public static final String GET_DEPARTMENT = BASE_URL+"department/query";//获取所有部门
    public static final String REGISTER = BASE_URL +"user/save?";//注册
    public static final String MEETING_ROOM = BASE_URL+"meeting/queryMeetingrooms";//获取会议室
    public static final String METTING_TYPES = BASE_URL+"meeting/queryServicetypes";//获取会议类型
    public static final String MEETIG_APPLY = BASE_URL+"meeting/save?";//申请会议
    public static final String MEETING_LIST = BASE_URL +"meeting/query?";//会议查询
    public static final String MEETING_ATTEND = BASE_URL+"meeting/queryRecord?";//会议出席记录
    public static final String MEETING_ADD_USERS = BASE_URL + "meeting/saveUser?";//添加人员，签到
    public static final String MEETING_GUARANTEE_RATING = BASE_URL + "meeting/updateDistribute?";
    public static final String WORK_LIST = BASE_URL + "distribute/query?";//我的工单
}
