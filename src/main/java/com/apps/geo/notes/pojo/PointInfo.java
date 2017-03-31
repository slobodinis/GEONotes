package com.apps.geo.notes.pojo;

import android.content.ContentValues;

import java.io.Serializable;

public class PointInfo implements Serializable {

    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private double radius;
    private boolean active;

    public PointInfo(){
        id = 0;
    }

    public PointInfo(String name, String description, double latitude, double longitude,
                     double radius, boolean active) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.active = active;
    }

    public PointInfo(int id, String name, String description, double latitude, double longitude,
                     double radius, boolean active) {
        this(name, description, latitude, longitude, radius, active);
        this.id = id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public double getRadius(){
        return radius;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return this.active;
    }

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("name", this.getName());
        cv.put("active", this.isActive() ? 1: 0);
        cv.put("description", this.getDescription());
        cv.put("latitude", this.getLatitude());
        cv.put("longitude", this.getLongitude());
        cv.put("radius", this.getRadius());
        return cv;
    }
}
