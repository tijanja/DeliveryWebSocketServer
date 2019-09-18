package com.tijanja.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response
{
    @SerializedName("action")
    @Expose
    String action;

    @SerializedName("data")
    @Expose
    LoginObject loginObject;

    public String getAction()
    {
        return action;
    }

    public LoginObject getLoginObject()
    {
        return loginObject;
    }
}