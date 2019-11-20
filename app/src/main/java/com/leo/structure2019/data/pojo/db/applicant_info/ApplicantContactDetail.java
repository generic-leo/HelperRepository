package com.leo.structure2019.data.pojo.db.applicant_info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "contact_detail")
public class ApplicantContactDetail {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    public int contactID;

    @ColumnInfo(name = "applicant_id")
    public int applicantID;

    public String flatSociety = "";
    public String roadName = "";
    public String areaLocality = "";
    public String landMark = "";
    public String district = "";
    public String pinCode = "";
    public String city = "";
    public String state = "";
    public String stdCode = "";
    public String residenceCode = "";
    public String residenceStatus = "";
    public String residenceLandline = "";
    public String emailID = "";
    public String mobileno = "";

    public String currFlatSociety = "";
    public String currRoadName = "";
    public String currAreaLocality = "";
    public String currLandMark = "";
    public String currDistrict = "";
    public String currPinCode = "";
    public String currCity = "";
    public String currState = "";

    public String perFlatSociety = "";
    public String perRoadName = "";
    public String perAreaLocality = "";
    public String perLandMark = "";
    public String perDistrict = "";
    public String perPinCode = "";
    public String perCity = "";
    public String perState = "";

    public String mailingAddress="";
}
