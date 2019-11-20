package com.leo.structure2019.data.pojo.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "branch_address")
public class BranchAddress {

    @PrimaryKey(autoGenerate = true)
    public int branchID;


    @ColumnInfo(name = "branch_name")
    public String branchName = "";

    @ColumnInfo(name = "branch_address")
    public String branchAddress = "";

    public BranchAddress(String branchName, String branchAddress) {
        super();
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }


/*
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }
*/


    @Override
    public String toString() {
        return "BranchAddress{" +
                "branchName='" + branchName + '\'' +
                ", branchAddress='" + branchAddress + '\'' +
                '}';
    }


}
