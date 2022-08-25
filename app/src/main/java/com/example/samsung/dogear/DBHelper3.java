package com.example.samsung.dogear;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2017-01-11.
 */
public class DBHelper3 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scheduler.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper3(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE scheduler ( _id INTEGER PRIMARY KEY" +
                " AUTOINCREMENT, date TEXT, schedule TEXT, condition TEXT );");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS scheduler");
        onCreate(db);
    }


}