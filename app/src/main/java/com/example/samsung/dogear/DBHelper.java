package com.example.samsung.dogear;
/**
 * Created by User on 2017-01-10.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "result.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE result ( _id INTEGER PRIMARY KEY" +
                " AUTOINCREMENT, date TEXT, dev TEXT, steps INTEGER, rest INTEGER, play INTEGER, act INTEGER, temp TEXT, bark INTEGER, battery INTEGER);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS result");
        onCreate(db);
    }

}
