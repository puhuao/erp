package com.managesystem.event;

import com.managesystem.model.MeetingRoom;

/**
 * Created by puhua on 2016/12/27.
 *
 * @
 */
public class MeetingRoomConditionSelectEvent {
    private MeetingRoom department;

    public MeetingRoom getDepartment() {
        return department;
    }

    public void setDepartment(MeetingRoom department) {
        this.department = department;
    }

    public MeetingRoomConditionSelectEvent(MeetingRoom department) {
        this.department = department;
    }
}
