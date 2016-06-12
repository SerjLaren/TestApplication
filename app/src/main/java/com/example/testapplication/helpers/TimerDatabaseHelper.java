package com.example.testapplication.helpers;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Сергей on 30.05.2016.
 */
public class TimerDatabaseHelper extends SQLiteOpenHelper {

    public TimerDatabaseHelper(Context context) {
        super(context, "myDBTimer", null, 1);
    }

    public TimerDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                   int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytableTimer ("
                + "_id integer primary key autoincrement,"
                + "hour text,"
                + "minut text,"
                + "second text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
