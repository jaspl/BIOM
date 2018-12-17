package com.example.jan.recognitionusingsensordata.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Manage the database
 */
public class DbAdapter extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String DB_NAME="dbname";
    private static final String TABLE_NAME="tableName";
    private static final String USERNAME="USERNAME";
    private static final String MOVEMENT1="MOVEMENT1";
    private static final String MOVEMENT2="MOVEMENT2";
    private static final String MOVEMENT3="MOVEMENT3";
    private static final String MOVEMENT4="MOVEMENT4";
    private static final String MOVEMENT5="MOVEMENT5";

    public DbAdapter(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME+ "(USERNAME TEXT,MOVEMENT1 TEXT,MOVEMENT2 TEXT,MOVEMENT3 TEXT,MOVEMENT4 TEXT,MOVEMENT5 TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    /**
     * Method inserts data to database
     * @param username
     * @param movement1
     * @param movement2
     * @param movement3
     * @param movement4
     * @param movement5
     * @return
     */
    public boolean insertData(String username, String movement1, String movement2, String movement3, String movement4, String movement5){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME,username);
        contentValues.put(MOVEMENT1,movement1);
        contentValues.put(MOVEMENT2,movement2);
        contentValues.put(MOVEMENT3,movement3);
        contentValues.put(MOVEMENT4,movement4);
        contentValues.put(MOVEMENT5,movement5);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Method gets values froma database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }
}
