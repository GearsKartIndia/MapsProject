package com.example.mymapsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper  extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, "mapApp.db" , null , 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createStatement="CREATE TABLE PLACES (PLACE TEXT, LATITUDE REAL, LONGITUDE REAL)";
        sqLiteDatabase.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insert(String place,double latitude,double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("PLACE",place);
        cv.put("LATITUDE",latitude);
        cv.put("LONGITUDE",longitude);

        long flag= db.insert("PLACES",null,cv);
        return flag != -1;
    }

    public void delete(String place){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PLACES","PLACE = '"+place+"'",null);
    }

    public List<Location1> selectAll(){
        List<Location1> allPlaces = new ArrayList<>();

        String query="SELECT * FROM PLACES";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                String place=cursor.getString(0);
                double latitude=cursor.getDouble(1);
                double longitude=cursor.getDouble(2);
                Location1 l= new Location1(place,latitude,longitude);
                if(!allPlaces.contains(l)) {
                    allPlaces.add(l);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return allPlaces;
    }
}
