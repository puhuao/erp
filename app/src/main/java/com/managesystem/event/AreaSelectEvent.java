package com.managesystem.event;

/**
 * Created by Administrator on 2016/12/19.
 */
public class AreaSelectEvent {
    String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public AreaSelectEvent(String area) {
        this.area = area;
    }
}
