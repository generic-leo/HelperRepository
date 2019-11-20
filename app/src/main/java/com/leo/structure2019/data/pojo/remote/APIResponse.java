package com.leo.structure2019.data.pojo.remote;

import com.google.gson.annotations.SerializedName;

public class APIResponse {
    @SerializedName("error_code")
    public String error_code = "-1";

    @SerializedName("error_message")
    public String error_message = "Received Invalid Response From Server";

    @SerializedName("data")
    public String data;
}
