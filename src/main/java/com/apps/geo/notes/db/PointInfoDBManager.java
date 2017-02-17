package com.apps.geo.notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apps.geo.notes.pojo.PointInfo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 1038844 on 11.02.2017.
 */

public class PointInfoDBManager implements DBConstants {

    private Context context;

    public PointInfoDBManager(Context context)
    {
        this.context = context;
    }

    public void insertPoint(PointInfo pointInfo)
    {
        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("name", pointInfo.getName());
        cv.put("description", pointInfo.getDescription());
        cv.put("lattitude", pointInfo.getLattitude());
        cv.put("longitude", pointInfo.getLongitude());
        cv.put("date", pointInfo.getDate().getTime());
        cv.put("term", pointInfo.getTerm());
        cv.put("radius",pointInfo.getRadius());
        try {
            db.insert(POINT_INFO, null, cv);
        } catch (Exception e)
        {
        }
        finally {
            db.close();
            dbHelper.close();
        }
    }

    public void deletePointById(int id)
    {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(POINT_INFO,"id = ?",new String[]{id+""});
        } catch (Exception e)
        {
        }
        finally {
            db.close();
            dbHelper.close();
        }
    }

    public ArrayList<PointInfo> getAllPoints()
    {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(POINT_INFO,null,null,null,null,null,null);
        ArrayList<PointInfo> points = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                points.add(new PointInfo(c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("description")),
                        c.getDouble(c.getColumnIndex("lattitude")),
                        c.getDouble(c.getColumnIndex("longitude")),
                        new Date(c.getLong(c.getColumnIndex("date"))),
                        c.getLong(c.getColumnIndex("term")),
                        c.getInt(c.getColumnIndex("radius"))));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        dbHelper.close();
        return points;
    }

    public PointInfo getPointById(int id)
    {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(POINT_INFO,null, "id = ?", new String[]{id + ""}, null, null, null);
        PointInfo pointInfo = null;
        if (c.moveToFirst())
        {
            pointInfo = new PointInfo(c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("description")),
                    c.getDouble(c.getColumnIndex("lattitude")),
                    c.getDouble(c.getColumnIndex("longitude")),
                    new Date(c.getLong(c.getColumnIndex("date"))),
                    c.getLong(c.getColumnIndex("term")),
                    c.getInt(c.getColumnIndex("radius")));
        }
        c.close();
        db.close();
        dbHelper.close();
        return pointInfo;
    }

    public void updatePointById(PointInfo pointInfo)
    {
        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("name", pointInfo.getName());
        cv.put("description", pointInfo.getDescription());
        cv.put("lattitude", pointInfo.getLattitude());
        cv.put("longitude", pointInfo.getLongitude());
        cv.put("date", pointInfo.getDate().getTime());
        cv.put("term", pointInfo.getTerm());
        cv.put("radius",pointInfo.getRadius());
        try {
            db.update(POINT_INFO, cv, "id = ?",new String[] { pointInfo.getId()+"" });
        } catch (Exception e)
        {
        }
        finally {
            db.close();
            dbHelper.close();
        }
    }

    public Context getContext() {
        return context;
    }
}
