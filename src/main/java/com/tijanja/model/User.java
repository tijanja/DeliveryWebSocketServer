package com.tijanja.model;

import java.io.Serializable;

import javax.websocket.Session;

public class User implements Serializable {

    boolean isOnRide,terms;
    String rideId;
    String id;
    String name;
    String phone;
    String city;
    String email;
    String type;
    Float  rating;
    String sessionId;
    Session session;
    String hexLoc;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    String imageUrl;

    
    public boolean isOnRide() {
        return isOnRide;
    }

    
    public void setOnRide(boolean onRide) {
        isOnRide = onRide;
    }

    
    public String getRideId() {
        return rideId;
    }

   
    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTerms() {
        return terms;
    }

    public void setTerms(boolean terms) {
        this.terms = terms;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSession(Session s)
    {
        session = s;
    }

    public Session getSession()
    {
        return session;
    }

    public void setLocation(String l)
    {
        hexLoc = l;
    }

    public boolean isLocationSet()
    {
        if(hexLoc!=null)
        {
            return true;
        }
        return false;
    }
}
