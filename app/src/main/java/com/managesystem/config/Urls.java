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
}
