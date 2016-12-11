package com.managesystem.event;

import com.managesystem.model.PPSModel;

/**
 * Created by Administrator on 2016/12/9.
 */
public class PPSDeleteEvent {
    PPSModel ppsModel;
    public PPSDeleteEvent(PPSModel ppsModel) {
        this.ppsModel = ppsModel;
    }
}
