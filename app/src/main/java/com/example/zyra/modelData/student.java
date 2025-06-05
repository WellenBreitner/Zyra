package com.example.zyra.modelData;

import android.net.Uri;

import com.example.zyra.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class student {
    private String studentId;
    private String name;
    private String email;
    private String status;
    private List<String> registeredSubjects;

    public student(){}

    public student(String name, String email) {
        this.studentId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.status = "Student";
        this.registeredSubjects = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRegisteredSubjects() {
        return registeredSubjects;
    }

    public void setRegisteredSubjects(List<String> registeredSubjects) {
        this.registeredSubjects = registeredSubjects;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
