package com.managesystem.event;

import com.managesystem.model.ResourcePersonModel;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ResourceSelectEcent {
    ResourcePersonModel model;

    public ResourcePersonModel getModel() {
        return model;
    }

    public void setModel(ResourcePersonModel model) {
        this.model = model;
    }

    public ResourceSelectEcent(ResourcePersonModel model) {
        this.model = model;
    }
}
