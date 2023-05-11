package com.Quadranet.myapplication.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EposResult
{
    @SerializedName("Foo1")
    @Expose
    public String foo1;
    @SerializedName("Foo2")
    @Expose
    public String foo2;
}
