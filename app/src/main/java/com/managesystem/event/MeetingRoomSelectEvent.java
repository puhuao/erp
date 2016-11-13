package com.managesystem.event;

import com.managesystem.model.Department;
import com.managesystem.model.MeetingRoom;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MeetingRoomSelectEvent {
    private MeetingRoom department;

    public MeetingRoom getDepartment() {
        return department;
    }

    public void setDepartment(MeetingRoom department) {
        this.department = department;
    }

    public MeetingRoomSelectEvent(MeetingRoom department) {
        this.department = department;
    }
}
