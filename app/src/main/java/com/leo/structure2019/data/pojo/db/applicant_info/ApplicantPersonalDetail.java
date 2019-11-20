package com.leo.structure2019.data.pojo.db.applicant_info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "personal_detail", indices = {@Index(value = {"applicant_id"},unique = true)})
public class ApplicantPersonalDetail {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "personal_id")
    public int personalID;

    @ColumnInfo(name = "applicant_id")
    public int applicantID;

    public String tiitle;
    public String gender;
    public String category;
    public String maritalStatus;
    public String religion;
    public String currentresidense;
    public String qualification;
    public String companytype;

    public String panNo = "";
    public String aadharNo = "";
    public String verifyPanno = "N";
    public String verifyAadharNo = "N";

    public String upload_image = "";
    public String firstName = "";
    public String middleName = "";
    public String lastName = "";
    public String mobileNo = "";
    public String dob = "";
    public String pob = "";
    public String child = "";
    public String other = "";
    public String years = "";
    public String months = "";
    public String fatherName = "";
    public String spouseName = "";

    public String category_desc = "";
    public String qualification_desc = "";
    public String religion_desc = "";

    public String noOfDependantes_child = "";
    public String noOfDependantes_other = "";

    @Override
    public String toString() {
        return "ApplicantPersonalDetail{" +
                "personalID=" + personalID +
                ", applicantID=" + applicantID +
                ", tiitle='" + tiitle + '\'' +
                ", gender='" + gender + '\'' +
                ", category='" + category + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", panNo='" + panNo + '\'' +
                ", aadharNo='" + aadharNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", dob='" + dob + '\'' +
                ", pob='" + pob + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", isAadhaarApproved=" + verifyAadharNo +
                ", isPanApproved=" + verifyPanno +
                '}';
    }
}
