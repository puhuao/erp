package com.managesystem.event;

import com.managesystem.model.Status;
import com.managesystem.popupwindow.ResourceStatusSelectPopupwindow;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ResStatusSelectEvent {
    public Status status;
    public ResStatusSelectEvent(Status status) {
        this.status = status;
    }
}
