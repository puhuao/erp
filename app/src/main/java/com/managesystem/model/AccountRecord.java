package com.managesystem.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/4.
 */
public class AccountRecord implements Serializable{

    /**
     * ctime : 2016-12-02 22:09
     * handleUserId : null
     * message : 成功消费3.0元。
     * money : 3
     * recordId : 26
     * recordUserId : 51c7dd3b5926472db1db7b87bcaa4c6f
     * type : 5
     */

    private String ctime;
    private String handleUserId;
    private String message;
    private float money;
    private int recordId;
    private String recordUserId;
    private int type;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getHandleUserId() {
        return handleUserId;
    }

    public void setHandleUserId(String handleUserId) {
        this.handleUserId = handleUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecordUserId() {
        return recordUserId;
    }

    public void setRecordUserId(String recordUserId) {
        this.recordUserId = recordUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
