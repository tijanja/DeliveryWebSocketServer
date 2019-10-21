package com.tijanja.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response
{
    @SerializedName("action")
    @Expose
    String action;

    public String getAction()
    {
        return action;
    }
}