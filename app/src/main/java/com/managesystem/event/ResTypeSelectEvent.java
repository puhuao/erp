package com.managesystem.event;

import com.managesystem.model.ResourceName;
import com.managesystem.model.ResourceType;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ResTypeSelectEvent {
    ResourceType resourceName;

    public ResTypeSelectEvent(ResourceType resourceName) {
        this.resourceName = resourceName;
    }

    public ResourceType getResourceName() {
        return resourceName;
    }

    public void setResourceName(ResourceType resourceName) {
        this.resourceName = resourceName;
    }
}
