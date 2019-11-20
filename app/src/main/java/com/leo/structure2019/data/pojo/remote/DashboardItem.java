package com.leo.structure2019.data.pojo.remote;

public class DashboardItem {
    // @SerializedName(value = "appid")
    public String appid = "";
    public String loanAmount = "";
    public String loanType = "";
    public String loanDate = "";
    public String level = "";
    public String status = "";

    @Override
    public String toString() {
        return "Status : "+ status;
    }
}

