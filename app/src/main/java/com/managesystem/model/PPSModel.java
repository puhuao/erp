package com.managesystem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class PPSModel implements Serializable{

    /**
     * content : 423432432
     * praiseCount : 4
     * pics : ["20161123/87c1b943c56d4ea8acf7cf88b41f5cdd","20161123/9728047463ef42cb8e4b1231085d9c88","20161123/2483944c3b3e4fee9a2b6683ef5eb153","20161123/363e1ace245d4bf3af3512bfd1281947","20161123/fb0f41e79caf48eaa42d6b97fed853de","20161123/73fbcf9a42be408e8d0a311653347c7d","20161123/326b11e37bf0493cada11949b94990dd","20161123/cb2aa91c9e8e479380e6fe4d1e9b0748","20161123/06950aa24b304dcfbdb44ef7f58128e3"]
     * title : Ceshi324
     * name : 牟
     * userId : 51c7dd3b5926472db1db7b87bcaa4c6f
     * topicId : f0b395f4212b4f6fa1204f35a5a02016
     * headPic : null
     * scanCount : 19
     * ctime : 2016-11-23 15:09
     */

    private String content;
    private int praiseCount;
    private String title;
    private String name;
    private String userId;
    private String topicId;
    private String headPic;
    private int scanCount;
    private String ctime;
    private List<String> pics;
    private Boolean isPraise;

    public Boolean getPraise() {
        return isPraise;
    }

    public void setPraise(Boolean praise) {
        isPraise = praise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
