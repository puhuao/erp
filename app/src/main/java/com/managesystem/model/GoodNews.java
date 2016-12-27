package com.managesystem.model;

import java.util.List;

/**
 * Created by puhua on 2016/11/11.
 *
 * @
 */

public class GoodNews {

    /**
     * title : xxxx
     * infor : xxxxx
     * status : 1
     * isNew : true
     * wealId : 22333
     * isApply : false
     * ctime : 2016-11-27 16:19
     */

    private String title;
    private String infor;
    private int status;
    private boolean isNew;
    private String wealId;
    private boolean isApply;
    private String ctime;
    private List<String> pics;

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getWealId() {
        return wealId;
    }

    public void setWealId(String wealId) {
        this.wealId = wealId;
    }

    public boolean isIsApply() {
        return isApply;
    }

    public void setIsApply(boolean isApply) {
        this.isApply = isApply;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
