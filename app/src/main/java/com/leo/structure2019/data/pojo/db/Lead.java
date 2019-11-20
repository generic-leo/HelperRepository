package com.leo.structure2019.data.pojo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "lead")
public class Lead {

    @PrimaryKey(autoGenerate = true)
    public int leadID;

    public String leadServerID = getUniqueAppNo();

    @ColumnInfo(name = "login_id")
    public String loginID;

    public String name = "";
    public String type = "";
    public String time = "";
//    public String leadid = "";
    public String status = "";
    public String loanAmount = "" ;
    public  String leadStatus = "";

    public String mobNo = "0";

    public Lead(){}

    @Ignore
    public Lead(String name){
        this.name = name;
    }

    public static String getUniqueAppNo(){
        String number = UUID.randomUUID().toString().toUpperCase();
        number = number.substring(0,number.indexOf("-"));
        return number;
    }
}
