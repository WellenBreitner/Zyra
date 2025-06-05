package com.example.zyra.modelData;

public class notification_item {

    String title,description;
    Boolean isRead;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public notification_item(String title, String description) {
        this.title = title;
        this.isRead = false;
        this.description = description;
    }
}
