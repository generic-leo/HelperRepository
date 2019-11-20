package com.leo.structure2019.data.pojo.db.applicant_info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "occupation_detail")
public class ApplicantOccupation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "occupation_id")
    public int occupationID;

    @ColumnInfo(name = "applicant_id")
    public int applicantID;

    public String  natureOfEmployment = "";
    public String  organisationName = "";
    public String  monthlyIncome = "";
    public String  addressLineOne = "";
    public String  addressLineTwo = "";
    public String  addressLineThree = "";
    public String  pinCode = "";

    public String  city = "";
    public String  state = "";
    public String  city_desc = "";
    public String  state_desc = "";

    public String  stdCode = "";
    public String  ofcLandlineNo = "";
    public String  extension = "";
    public String  ofcEmail = "";
    public String  designation = "";
    public String  tweYears = "";
    public String  tweMonths = "";
    public String  dibYears = "";
    public String  dibMonths = "";
    public String  country = "";

    public String officeCountry_desc = "";
    public String companyName_desc = "";
}
