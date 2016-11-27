package com.managesystem.event;

import com.managesystem.model.ResourcePersonModel;

import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ResourceSelectEvent {
    List<ResourcePersonModel> model;

    public ResourceSelectEvent(List<ResourcePersonModel> model) {
        this.model = model;
    }

    public List<ResourcePersonModel> getModel() {
        return model;
    }

    public void setModel(List<ResourcePersonModel> model) {
        this.model = model;
    }
}
