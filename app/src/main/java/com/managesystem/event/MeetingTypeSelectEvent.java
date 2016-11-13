package com.managesystem.event;

import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingType;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MeetingTypeSelectEvent {
    private MeetingType department;

    public MeetingType getDepartment() {
        return department;
    }

    public void setDepartment(MeetingType department) {
        this.department = department;
    }

    public MeetingTypeSelectEvent(MeetingType department) {
        this.department = department;
    }
}
