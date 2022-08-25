package com.example.samsung.dogear;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by smart-factory on 2017-08-14.
 */

public class DBPhoto extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "photo.db";
    private static final int DATABASE_VERSION = 2;

    public DBPhoto(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE photo (_id INTEGER PRIMARY KEY" +
                " AUTOINCREMENT, img blob not null, name text not null);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS result");
        onCreate(db);
    }


}
