package com.managesystem.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/13.
 */
public class Users implements Serializable{


    /**
     * name : 三
     * userId : 69177258a06e4927b4639ab1684c3320
     * type : 2
     */

    private String name;
    private String userId;
    private String type;
    public Boolean isCheck = false;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
