package com.leo.structure2019.data.pojo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "applicant")
public class Applicant {

    @PrimaryKey()
    @ColumnInfo(name = "applicant_id")
    public int applicantID;

    @ColumnInfo(name = "server_applicant_id")
    public int serverApplicantID;

    @ColumnInfo(name = "application_no")
    public int applicationNo;

    @ColumnInfo(name = "applicant_name")
    public String name = "";

    public String imageName;

    public String applicantStatus = "primary";

    public String getName() {
        return name;
    }

    public Applicant setName(String name) {
        this.name = name;
        return this;
    }

    public Applicant imageName(String imageFileName){
        this.imageName = imageFileName;
        return this;
    }

    public int getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(int applicantID) {
        this.applicantID = applicantID;
    }

    public int getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(int applicationNo) {
        this.applicationNo = applicationNo;
    }
}
