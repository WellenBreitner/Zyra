package com.example.zyra.modelData;

import java.io.Serializable;
import java.util.UUID;

public class material implements Serializable {
    private String materialId;
    private String materialClassCode;
    private String materialName;
    private String materialPostDate;
    private String materialDesc;
    private String materialMeetLink;

    public material(String materialClassCode, String materialName, String materialPostDate, String materialDesc, String materialMeetLink) {
        this.materialId = UUID.randomUUID().toString();
        this.materialClassCode = materialClassCode;
        this.materialName = materialName;
        this.materialPostDate = materialPostDate;
        this.materialDesc = materialDesc;
        this.materialMeetLink = materialMeetLink;
    }
    
    public material(){
        
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialClassCode() {
        return materialClassCode;
    }

    public void setMaterialClassCode(String materialClassCode) {
        this.materialClassCode = materialClassCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialPostDate() {
        return materialPostDate;
    }

    public void setMaterialPostDate(String materialPostDate) {
        this.materialPostDate = materialPostDate;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialMeetLink() {
        return materialMeetLink;
    }

    public void setMaterialMeetLink(String materialMeetLink) {
        this.materialMeetLink = materialMeetLink;
    }
}
