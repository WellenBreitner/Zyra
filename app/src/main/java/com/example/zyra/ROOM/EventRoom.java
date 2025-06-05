package com.example.zyra.ROOM;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "events")
public class EventRoom implements Serializable {
    @PrimaryKey
    @NonNull
    public String eventId;
    public String eventName;
    public String eventType;
    public String eventClassCode;
    public String eventDeadlineDate;
    public String eventDeadlineTime;
    public String eventDesc;
    public String eventSubmissionLink;
    public int eventImage;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventClassCode() {
        return eventClassCode;
    }

    public void setEventClassCode(String eventClassCode) {
        this.eventClassCode = eventClassCode;
    }

    public String getEventDeadlineDate() {
        return eventDeadlineDate;
    }

    public void setEventDeadlineDate(String eventDeadlineDate) {
        this.eventDeadlineDate = eventDeadlineDate;
    }

    public String getEventDeadlineTime() {
        return eventDeadlineTime;
    }

    public void setEventDeadlineTime(String eventDeadlineTime) {
        this.eventDeadlineTime = eventDeadlineTime;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventSubmissionLink() {
        return eventSubmissionLink;
    }

    public void setEventSubmissionLink(String eventSubmissionLink) {
        this.eventSubmissionLink = eventSubmissionLink;
    }

    public int getEventImage() {
        return eventImage;
    }

    public void setEventImage(int eventImage) {
        this.eventImage = eventImage;
    }

    public EventRoom(String eventId, String eventName, String eventType, String eventClassCode,
                     String eventDeadlineDate, String eventDeadlineTime, String eventDesc,
                     String eventSubmissionLink, int eventImage) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventClassCode = eventClassCode;
        this.eventDeadlineDate = eventDeadlineDate;
        this.eventDeadlineTime = eventDeadlineTime;
        this.eventDesc = eventDesc;
        this.eventSubmissionLink = eventSubmissionLink;
        this.eventImage = eventImage;
    }

}
