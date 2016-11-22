package com.managesystem.event;

import com.managesystem.model.MeetingRoom;
import com.managesystem.model.MeetingType;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MeetingTypeSelectEvent {

    private MeetingType meetingType;
    private String equipmentName;

    public MeetingTypeSelectEvent(MeetingType meetingType, String equipmentName) {
        this.meetingType = meetingType;
        this.equipmentName = equipmentName;
    }

    public MeetingType getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}

