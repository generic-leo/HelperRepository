package com.leo.structure2019.data.pojo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "loan_details")
public class LoanDetail {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "detail_id")
    public int loanDetailID;

    @ColumnInfo(name = "application_no")
    public int applicationNo;

    public String amountRequested = "";
    public String term = "";
    public String typeOfInterest = "";
    public String product = "";
    public String promotion = "";
    public String purposeOfLoan = "";
}
