package com.apps.geo.notes.pojo;

import java.util.Date;

/**
 * Created by 1038844 on 11.02.2017.
 */

public class PointInfo {

    private int id;
    private String name;
    private String description;
    private long term;
    private double lattitude;
    private double longitude;
    private Date date;
    private double radius;

    public PointInfo(String name, String description, double lattitude, double longitude, double radius)
    {
        this.name = name;
        this.description = description;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.radius = radius;
        term = -1;
        date = new Date();
    }

    public PointInfo(String name, String description, double lattitude, double longitude, Date date, long term, double radius)
    {
        this.name = name;
        this.description = description;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.radius = radius;
        this.date = date;
        this.term = term;
    }
    public PointInfo(int id,String name, String description, double lattitude, double longitude, Date date, long term, double radius)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.date = date;
        this.term = term;
        this.radius = radius;
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

    public void setLattitude(long lattitude)
    {
        this.lattitude = lattitude;
    }

    public double getLattitude()
    {
        return lattitude;
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

    public double getRadius()
    {
        return radius;
    }


}
