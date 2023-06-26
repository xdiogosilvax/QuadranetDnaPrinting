package com.Quadranet.myapplication.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EposResult
{
    @SerializedName("bill_data")
    @Expose
    public String DataStr;
    @SerializedName("error_message")
    @Expose
    public String error_message;
}
