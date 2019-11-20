package com.leo.structure2019.data.pojo.db.applicant_info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "financial_detail")
public class ApplicantFinancial {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "financial_id")
    public int financialID;

    @ColumnInfo(name = "applicant_id")
    public int applicantID;

    public String liveTypeOfAccount = "";
    public String liveLoanTakenAgainst = "";

    public String accountHolderName = "";
    public String bankName = "";
    public String branchName = "";
    public String accountNumber = "";
    public String lenderName = "";
    public String emi = "";
    public String outstandingAmount = "";
    public String outstandingTenure = "";
}
