package com.tijanja.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationResponse
{
    @SerializedName("action")
    @Expose
    String action;

    @SerializedName("sessionId")
    @Expose
    String session;

    @SerializedName("data")
    @Expose
    Location location;

    public String getAction()
    {
        return action;
    }

    public Location getLocation()
    {
        return location;
    }

    public String getSessionId()
    {
        return session;
    }
}