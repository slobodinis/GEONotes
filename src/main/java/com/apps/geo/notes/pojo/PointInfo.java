package com.apps.geo.notes.pojo;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.Date;

public class PointInfo implements Serializable {

    private boolean active;
    private int id;
    private String name;
    private String description;
    private long term;
    private double latitude;
    private double longitude;
    private Date date;
    private double radius;

    public PointInfo(String name, String description, double latitude, double longitude, Date date,
                     long term, double radius, boolean active) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.date = date;
        this.term = term;
        this.active = active;
    }

    public PointInfo(String name, String description, double latitude, double longitude, Date date,
                     long term, double radius) {
        this(name, description, latitude, longitude, date, term, radius, true);
    }

    public PointInfo(String name, String description, double latitude, double longitude, double radius) {
        this(name, description, latitude, longitude, new Date(), -1, radius, true);
    }

    public PointInfo(int id, String name, String description, double latitude, double longitude, Date date, long term, double radius, boolean active) {
        this(name, description, latitude, longitude, date, term, radius, active);
        this.id = id;
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

    public void setTerm(long term)
    {
        this.term = term;
    }

    public long getTerm()
    {
        return term;
    }

    public void setLatitude(long latitude)
    {
        this.latitude = latitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLongitude(long longitude)
    {
        this.longitude = longitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setDate(long date)
    {
        this.date = new Date(date);
    }

    public Date getDate()
    {
        return date;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public double getRadius(){
        return radius;
    }

    public void enableAlarm(){
        this.active = true;
    }

    public void disableAlarm(){
        this.active = false;
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
        cv.put("date", this.getDate().getTime());
        cv.put("term", this.getTerm());
        cv.put("radius", this.getRadius());
        return cv;
    }
}
