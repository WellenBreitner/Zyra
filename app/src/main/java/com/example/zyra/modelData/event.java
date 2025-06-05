package com.example.zyra.modelData;

import com.example.zyra.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class event implements Serializable {
    private String eventId;
    private String eventName;
    private String eventType;
    private String eventClassCode;
    private String eventDeadlineDate;
    private String eventDeadlineTime;
    private String eventDesc;
    private String eventSubmissionLink;
    private int eventImage;


    public event(String eventName, String eventType, String eventClassCode, String eventDeadlineDate, String eventDeadlineTime, String eventDesc, String eventSubmissionLink) {
        this.eventId = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventClassCode = eventClassCode;
        this.eventDeadlineDate = eventDeadlineDate;
        this.eventDeadlineTime = eventDeadlineTime;
        this.eventDesc = eventDesc;
        this.eventSubmissionLink = eventSubmissionLink;
        this.eventImage = getEventImageBasedOnType(eventType);
    }

    public event() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

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
        this.eventImage = getEventImageBasedOnType(eventType);
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

    private int getEventImageBasedOnType(String eventType) {
        switch (eventType) {
            case "Assignment":
                return R.drawable.assignment_icon;
            case "Lab Test":
                return R.drawable.test_icon;
            case "Exam":
            case "Written Test":
                return R.drawable.exam_icon;
            default:
                return R.drawable.assignment_icon;
        }
    }
}
