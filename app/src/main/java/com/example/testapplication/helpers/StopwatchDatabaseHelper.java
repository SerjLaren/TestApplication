package com.example.testapplication.helpers;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Сергей on 30.05.2016.
 */
public class StopwatchDatabaseHelper extends SQLiteOpenHelper {

    public StopwatchDatabaseHelper(Context context) {
        super(context, "myDBStopwatch", null, 1);
    }

    public StopwatchDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                   int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytableStopwatch ("
                + "_id integer primary key autoincrement,"
                + "minut text,"
                + "second text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
