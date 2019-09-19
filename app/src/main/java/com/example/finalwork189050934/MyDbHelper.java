package com.example.finalwork189050934;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String CREATE_USER="create table user (" +
                                        "id integer primary key autoincrement," +
                                "account text," +
                                        "password text)";
    private static final String CREATE_INCOME="create table income("+
                                        "id integer primary key autoincrement,"+
                                        "account text,"+
                                        "productName text,"+
                                        "money decimal,"+
                                        "time text,"+
                                        "type text," +
                                        "mark text)";
    private static final String CREATE_EXPEND="create table expend("+
                                        "id integer primary key autoincrement,"+
                                        "account text,"+
                                        "productName text,"+
                                        "money decimal,"+
                                        "time text,"+
                                        "type text," +
                                        "mark text)";

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INCOME);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_EXPEND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists income");
        db.execSQL("drop table if exists expend");
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_INCOME);
        db.execSQL(CREATE_EXPEND);
    }
}
