package com.example.testapplication.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapplication.R;
import com.example.testapplication.adapters.RecyclerAdapter;
import com.example.testapplication.helpers.StopwatchDatabaseHelper;
import com.example.testapplication.helpers.TimerDatabaseHelper;

import java.util.ArrayList;

public class SavedTimersFragment extends Fragment {

    private ContentValues cv;
    private TimerDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor c;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList data;
    private final static String SECONDS_DB = "second", MINUTS_DB = "minut", HOURS_DB = "hour", DB_NAME = "mytableTimer";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_timers, container, false);
        initDB();
        initViews(v);
        return v;
    }

    private void initDB() {
        cv = new ContentValues();
        dbHelper = new TimerDatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        c = db.query(DB_NAME, null, null, null, null, null, null);
        getAllFromDB();
    }

    private void getAllFromDB() {
        data = new ArrayList();
        if (c.moveToFirst()) {
            do {
                data.add(getString(R.string.titleHourNotif) + c.getString(c.getColumnIndex(HOURS_DB)) + " " +
                        getString(R.string.titleMinNotif) + c.getString(c.getColumnIndex(MINUTS_DB)) + " " +
                        getString(R.string.titleSecNotif) + c.getString(c.getColumnIndex(SECONDS_DB)));
            } while (c.moveToNext());
        }
    }

    private void initViews(View v) {
        myRecyclerView = (RecyclerView) v.findViewById(R.id.rv_saved_timers);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getActivity(), data);
        myRecyclerView.setAdapter(mAdapter);
        myRecyclerView.setHasFixedSize(true);
    }
}
