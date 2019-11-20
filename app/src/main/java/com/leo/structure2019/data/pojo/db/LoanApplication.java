package com.leo.structure2019.data.pojo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Random;

@Entity(tableName = "loan_applications")
public class LoanApplication {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "loan_id")
    public int loanID;

    @ColumnInfo(name = "server_loan_id")
    public int serverLoanID;

    @ColumnInfo(name = "login_id")
    public String loginID;

    @ColumnInfo(name = "level")
    public String level = "";

    @ColumnInfo(name = "application_no")
    public String applicationNo = getUniqueAppNo();

    @ColumnInfo(name = "time")
    public String time = "";

    @ColumnInfo(name = "amount")
    public String amount = "";

    @ColumnInfo(name = "status")
    public String status = "Approved";

    @ColumnInfo(name = "review")
    public boolean review = false;

    @ColumnInfo(name = "approval")
    public boolean approval = false;

    @ColumnInfo(name = "payment")
    public boolean payment = false;

    public String loanType = "";

    @ColumnInfo(name = "sanctioned")
    public boolean sanctioned = new Random().nextBoolean();

    @ColumnInfo(name = "disbursement")
    public boolean disbursed = new Random().nextBoolean();


    public static String getUniqueAppNo(){
        // String number = UUID.randomUUID().toString().toUpperCase();
        // number = number.substring(0,number.indexOf("-"));
        // return number;
        return "";
    }
}
