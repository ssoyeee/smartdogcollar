package com.example.samsung.dogear;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "doginfo.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE doginfo ( _id INTEGER PRIMARY KEY" +
                " AUTOINCREMENT, dname TEXT, dbirth TEXT, dweight TEXT, dheight TEXT, dlength TEXT, dgender TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS doginfo");
        onCreate(db);
    }

    public ArrayList<doginfo> getAlldata() {
        ArrayList<doginfo> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM doginfo;", null);
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            arrayList.add(new doginfo(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5),res.getString(6)));
            res.moveToNext();
        }
        return arrayList;
    }

    class doginfo {
        String dname, dweight, dheight, dlength, dgender, dbrith;
        public doginfo(String dname, String dbrith, String dweight, String dheight, String dlength, String dgender) {
            this.dname=dname;
            this.dbrith=dbrith;
            this.dweight=dweight;
            this.dheight=dheight;
            this.dlength=dlength;
            this.dgender=dgender;
        }
    }
    public Cursor getCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT dname, dbrith FROM doginfo;", null);
    }
}