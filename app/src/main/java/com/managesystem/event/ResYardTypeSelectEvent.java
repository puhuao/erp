package com.managesystem.event;

import com.managesystem.model.ResourceType;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ResYardTypeSelectEvent {
    ResourceType resourceName;

    public ResYardTypeSelectEvent(ResourceType resourceName) {
        this.resourceName = resourceName;
    }

    public ResourceType getResourceName() {
        return resourceName;
    }

    public void setResourceName(ResourceType resourceName) {
        this.resourceName = resourceName;
    }
}
