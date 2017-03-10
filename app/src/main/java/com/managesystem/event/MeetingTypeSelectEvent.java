package com.managesystem.event;

import com.managesystem.model.MeetingType;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MeetingTypeSelectEvent {

    private MeetingType meetingType;
    private String equipmentName;
    private String equipmentIds;

    public String getEquipmentIds() {
        return equipmentIds;
    }

    public void setEquipmentIds(String equipmentIds) {
        this.equipmentIds = equipmentIds;
    }

    public MeetingTypeSelectEvent(MeetingType meetingType, String equipmentName, String id) {
        this.meetingType = meetingType;
        this.equipmentName = equipmentName;
        this.equipmentIds = id;
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

