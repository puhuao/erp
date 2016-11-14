package com.managesystem.model;

/**
 * Created by Administrator on 2016/11/14.
 */
public class QRCodeModel {
    private int type;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
