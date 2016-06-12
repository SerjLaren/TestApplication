package com.example.testapplication.fragments;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapplication.helpers.StopwatchDatabaseHelper;
import com.example.testapplication.adapters.RecyclerAdapter;
import com.example.testapplication.helpers.SharedPreferencesHelper;
import com.example.testapplication.services.StopwatchService;
import com.example.testapplication.R;

import java.util.ArrayList;

public class StopwatchFragment extends Fragment implements View.OnClickListener{

    private int second, minut;
    public boolean timerStoped = true;
    private String strMinut = "", strSecond = "", titleMinuts = "";
    private TextView tvSeconds, tvMinuts;
    private BroadcastReceiver br;
    private SharedPreferencesHelper prefs;
    private final static String SECONDS = "seconds", MINUTS = "minuts", TIMER_STOPED = "stopwatchStoped";
    public final static String CONNECTION_TO_TIMERSERVICE = "com.example.develop.fragment_secondsservice";
    private final static String SECONDS_DB = "second", MINUTS_DB = "minut", DB_NAME = "mytableStopwatch";
    private Button buttonStart, buttonStop, buttonTakeTime;
    private ContentValues cv;
    private StopwatchDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor c;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList data;
    private RecyclerView.ItemAnimator itemAnimator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        prefs = new SharedPreferencesHelper(getActivity());
        initDB();
        initViews(v);
        initValues();
        initReciever();
        IntentFilter intFilt = new IntentFilter(CONNECTION_TO_TIMERSERVICE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, intFilt);
        return v;
    }

    public interface OnFragmentStopwatchStartListener { // интерфейс для передачи активности сообщения об остановке таймера
        public void onFragmentStopwatchStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                startButton();
                break;
            case R.id.btnStop:
                stopButton();
                break;
            case R.id.btnTakeTime:
                takeTimeButton();
                break;
        }
    }

    private void getAllFromDB() {
        data = new ArrayList();
        if (c.moveToFirst()) {
            do {
                data.add(c.getString(c.getColumnIndex(MINUTS_DB)) + " " + c.getString(c.getColumnIndex(SECONDS_DB)));
            } while (c.moveToNext());
        }
    }

    private void startButton() {
        if (timerStoped) {
            OnFragmentStopwatchStartListener listener = (OnFragmentStopwatchStartListener) getActivity();
            listener.onFragmentStopwatchStart();
            getActivity().startService(new Intent(getActivity(), StopwatchService.class));
            prefs.setTimerStopedFALSE(TIMER_STOPED);
            timerStoped = false;
            buttonStart.setPressed(true);
            buttonStop.setPressed(false);
        }
    }

    public void stopButton() {
        timerStoped = true;
        buttonStart.setPressed(false);
        buttonStop.setPressed(true);
        prefs.setTimerStopedTRUE(TIMER_STOPED);
        getActivity().stopService(new Intent(getActivity(), StopwatchService.class));
        int clearCount = db.delete(DB_NAME, null, null);
        data.clear();
        mAdapter.notifyItemRangeRemoved(0, c.getCount());
    }

    private void takeTimeButton() {
        if (!timerStoped) {
            String sm = String.valueOf(minut);
            String ss = String.valueOf(second);
            cv.put(MINUTS_DB, sm);
            cv.put(SECONDS_DB, ss);
            long rowID = db.insert(DB_NAME, null, cv);
            db = dbHelper.getReadableDatabase();
            c = db.query(DB_NAME, null, null, null, null, null, null);
            c.moveToLast();
            data.add(c.getString(c.getColumnIndex(MINUTS_DB)) + " " + c.getString(c.getColumnIndex(SECONDS_DB)));
            mAdapter.notifyItemInserted(data.size());
        }
    }

    private void initDB() {
        cv = new ContentValues();
        dbHelper = new StopwatchDatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        c = db.query(DB_NAME, null, null, null, null, null, null);
        getAllFromDB();
    }

    private void initViews(View v) {
        myRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getActivity(), data);
        myRecyclerView.setAdapter(mAdapter);
        myRecyclerView.setHasFixedSize(true);
        itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(500);
        myRecyclerView.setItemAnimator(itemAnimator);
        buttonStart = (Button) v.findViewById(R.id.btnStart);
        buttonStop = (Button) v.findViewById(R.id.btnStop);
        buttonTakeTime = (Button) v.findViewById(R.id.btnTakeTime);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonTakeTime.setOnClickListener(this);
        tvSeconds = (TextView) v.findViewById(R.id.textviewSecondsFR);
        tvMinuts = (TextView) v.findViewById(R.id.textviewMinutsFR);
        tvMinuts.setText("");
        tvSeconds.setText("");
        initPressedButton();
    }

    private void initValues() {
        titleMinuts = getString(R.string.minutsStr);
        strMinut = Integer.toString(minut);
        strSecond = Integer.toString(second);
    }

    private void initPressedButton() {
        if (prefs.getTimerStoped(TIMER_STOPED)) {
            prefs.setTimerStopedTRUE(TIMER_STOPED);
            timerStoped = true;
            buttonStart.setPressed(false);
            buttonStop.setPressed(true);
        } else {
            timerStoped = false;
            buttonStart.setPressed(true);
            buttonStop.setPressed(false);
        }
    }

    private void initReciever() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // действия при получении данных от сервиса
                timerStoped = false;
                prefs.setTimerStopedFALSE(TIMER_STOPED);
                buttonStart.setPressed(true);
                buttonStop.setPressed(false);
                second = intent.getIntExtra(SECONDS, 0);
                minut = intent.getIntExtra(MINUTS, 0);
                strMinut = Integer.toString(minut);
                strSecond = Integer.toString(second);
                tvMinuts.setText(titleMinuts + strMinut);
                tvSeconds.setText(strSecond);
            }
        };
    }
}
