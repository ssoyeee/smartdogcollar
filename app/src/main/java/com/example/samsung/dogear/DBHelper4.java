package com.example.samsung.dogear;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 이지영 on 2017-04-27.
 */

public class DBHelper4 extends SQLiteOpenHelper  {

        private static final String DATABASE_NAME = "strength.db";
        private static final int DATABASE_VERSION = 2;

        public DBHelper4(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE strength ( _id INTEGER PRIMARY KEY" +
                    " AUTOINCREMENT, step TEXT, kind TEXT, level TEXT );");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS strength");
            onCreate(db);
        }

    class strength {
        String step, kind, stren;
        public strength(String step, String kind, String stren) {
            this.step=step;
            this.kind=kind;
            this.stren=stren;
        }
    }

}
