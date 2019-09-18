package com.tijanja.websocket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginObject{

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("password")
    @Expose
    String password;

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

}