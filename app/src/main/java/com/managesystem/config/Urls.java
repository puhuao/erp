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
    public static final String RESOURCE_TYPE = BASE_URL +"material/queryMaterialType";//物资类型
    public static final String RESOURCE_NAME = BASE_URL +"material/queryMaterialName?";//物资名称
    public static final String RESOURCE_APPLY = BASE_URL +"material/saveOrder";//物资申请(挂失)
    public static final String RESOURCE_APPLY_LIST = BASE_URL + "material/queryOrder?";//物资申请列表(挂失)
    public static final String RESOURCE_LIST = BASE_URL+"material/query?";//物资列表
    public static final String UPLOAD = "http://101.200.172.229:8080/ERP/appuser/savePic";//上传文件
    public static final String MSG_LIST = BASE_URL+"getMessages?";//获取消息列表
    public static final String UPDATE_MSG_STATUS = BASE_URL +"updateMessageStatus?";//修改消息状态
    public static final String RESOURCE_SEND_TRANSFER = BASE_URL+"material/saveRecord";//物资发放和交接
    public static final String MAINTAIN_APPLY = BASE_URL + "order/save?";//申请运维服务
    public static final String MAINTAIN_LIST_DETAIL = BASE_URL+"order/query?";//我的运维列表或详情
    public static final String PPS_POST = BASE_URL +"topic/save?";//发帖
    public static final String PPS_LIST = BASE_URL+"topic/query?";//帖子列表
    public static final String PPS_COMMECT = BASE_URL +"topic/saveReply?";//回帖
    public static final String PPS_COMMENT_LIST = BASE_URL +"topic/queryReplys?";//回帖列表
    public static final String PPS_ZAN = BASE_URL +"topic/praise?";//点赞
    public static final String GETPICS = "http://101.200.172.229:8080/ERP/appuser/getPic?path=";//获取图片
    public static final String GOOD_NEWS = BASE_URL+"weal/query?";//福利列表
    public static final String WEAL_SIGN = BASE_URL +"weal/saveUser?";//福利报名或查看
    public static final String PHONE_NUMBERS = BASE_URL +"department/queryUser?";//获取部门电话博
    public static final String MY_ACCOUNT =  BASE_URL +"wallet/query?";//我的账户
    public static final String MY_ACCOUNT_RECORD = BASE_URL+"wallet/queryRecord?";//我的账户记录
    public static final String GET_PAY_QRCODE = BASE_URL +"wallet/getQd";//生成付款码
    public static final String SAVE_PAY_RECORD = BASE_URL+"wallet/saveRecord?";//添加消费记录
    public static final String SAVE_USER = BASE_URL+"user/save?";//修改用户其他信息
    public static final String DELETE_TOPIC = BASE_URL+"topic/delete?";//删除帖子
    public static final String MARK_ALL = BASE_URL +"updateMyAllMessageStatus?";//一键阅读
    public static final String CHECK_NOT_COMMENT = BASE_URL+"distribute/queryNoEvaluate?";//检查是否有未评价的工单
    public static final String GETAREA = BASE_URL +"getAreas";//获取区域
    public static final String CHECK_VERSION = BASE_URL +"queryVersion?";
}
