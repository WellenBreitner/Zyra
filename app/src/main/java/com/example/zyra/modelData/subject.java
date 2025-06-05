package com.example.zyra.modelData;

import android.net.Uri;

import com.example.zyra.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class subject implements Serializable {
    private String subjectId;
    private String className;
    private String classCode;
    private String classPassword;
    private String lecturerName;
    private String lecturerAvatarUrl;
    private List<String> materials;
    private List<String> events;

    public subject() {
        this.materials = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public subject(String className, String classCode, String classPassword, String lecturerName, Uri lecturerAvatarUrl) {
        this.subjectId = UUID.randomUUID().toString();
        this.className = className;
        this.classCode = classCode;
        this.classPassword = classPassword;
        this.lecturerName = lecturerName;
        this.lecturerAvatarUrl = (lecturerAvatarUrl != null) ? lecturerAvatarUrl.toString() : null;
        this.materials = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassPassword() {
        return classPassword;
    }

    public void setClassPassword(String classPassword) {
        this.classPassword = classPassword;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getLecturerAvatarUrl() {
        return lecturerAvatarUrl;
    }

    public void setLecturerAvatarUrl(String lecturerAvatarUrl) {
        this.lecturerAvatarUrl = lecturerAvatarUrl;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}
