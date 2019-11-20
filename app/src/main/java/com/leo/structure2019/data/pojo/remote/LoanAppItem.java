package com.leo.structure2019.data.pojo.remote;

public class LoanAppItem {
    public String loanType;
    public int loanID;
    public String appid;
    public String loanAmount;
    public String status;
    public String loanDate;
    public String level;


//    @Override
//    public String toString() {
//        return "Status : "+ status;
//    }

    @Override
    public String toString() {
        return "LoanAppItem{" +
                "loanType='" + loanType + '\'' +
                ", loanID='" + loanID + '\'' +
                ", appid='" + appid + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", status='" + status + '\'' +
                ", loanDate='" + loanDate + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
