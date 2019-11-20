package com.leo.structure2019.data.pojo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "disbursement")
public class Disbursement {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "disbursement_id")
    public int disbursementID;

    @ColumnInfo(name = "application_no")
    public int applicationNo;

    @ColumnInfo(name = "agreement_amount")
    public int agreementValue;

    @ColumnInfo(name = "stamp_duty")
    public int stampDuty;

    @ColumnInfo(name = "registration_amount")
    public int registrattionAmount;

    @ColumnInfo(name = "other")
    public int other;

    @ColumnInfo(name = "amount_of_rs")
    public int amountOfRs;

    @ColumnInfo(name = "mode_of_payment")
    public String modeOfPayment;

    @ColumnInfo(name = "favouring")
    public String favouring;
}
