package com.example.zyra.modelData;

import android.net.Uri;

import com.example.zyra.R;

public class Admin {
    private String name;
    private String status;
    private String email;

    public Admin(String name, String email) {
        this.name = name;
        this.email = email;
        this.status = "Admin";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
