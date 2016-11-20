package com.managesystem.event;

import com.managesystem.model.ResourceName;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ResNameSelectEvent {
    ResourceName resourceName;

    public ResNameSelectEvent(ResourceName resourceName) {
        this.resourceName = resourceName;
    }

    public ResourceName getResourceName() {
        return resourceName;
    }

    public void setResourceName(ResourceName resourceName) {
        this.resourceName = resourceName;
    }
}
