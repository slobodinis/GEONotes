package com.apps.geo.notes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apps.geo.notes.pojo.PointInfo;

import java.util.ArrayList;

public class PointInfoDBManager implements DBConstants {

    private Context context;

    public PointInfoDBManager(Context context)
    {
        this.context = context;
    }

    public void insertPoint(PointInfo pointInfo){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.insert(POINT_INFO, null, pointInfo.toContentValues());
        } catch (Exception e) {
            e.printStackTrace();
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
            db.delete(POINT_INFO, "id = ?", new String[]{id+""});
        } catch (Exception e) {
            e.printStackTrace();
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
        Cursor c = db.query(POINT_INFO, null, null, null, null, null, null);
        ArrayList<PointInfo> points = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                points.add(getFromCursor(c));
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
        Cursor c = db.query(POINT_INFO, null, "id = ?", new String[]{id + ""}, null, null, null);
        PointInfo pointInfo = null;
        if (c.moveToFirst()){
            pointInfo = getFromCursor(c);
        }
        c.close();
        db.close();
        dbHelper.close();
        return pointInfo;
    }

    private PointInfo getFromCursor(Cursor c){
        return new PointInfo(c.getInt(c.getColumnIndex("id")),
                c.getString(c.getColumnIndex("name")),
                c.getString(c.getColumnIndex("description")),
                c.getDouble(c.getColumnIndex("latitude")),
                c.getDouble(c.getColumnIndex("longitude")),
                c.getInt(c.getColumnIndex("radius")),
                c.getInt(c.getColumnIndex("active")) != 0);
    }

    public void updatePointById(PointInfo pointInfo)
    {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.update(POINT_INFO, pointInfo.toContentValues(), "id = ?", new String[] {
                    String.valueOf(pointInfo.getId())
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.close();
            dbHelper.close();
        }
    }

    public ArrayList<PointInfo> getActivePoints()
    {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(POINT_INFO, null, "active=?", new String[]{"1"}, null, null, null);
        ArrayList<PointInfo> points = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                points.add(getFromCursor(c));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        dbHelper.close();
        return points;
    }

    public Context getContext() {
        return context;
    }
}
