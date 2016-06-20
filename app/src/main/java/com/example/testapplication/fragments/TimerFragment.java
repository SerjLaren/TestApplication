package com.example.testapplication.fragments;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.helpers.MyNumberPicker;
import com.example.testapplication.helpers.SharedPreferencesHelper;
import com.example.testapplication.helpers.StopwatchDatabaseHelper;
import com.example.testapplication.helpers.TimerDatabaseHelper;
import com.example.testapplication.services.TimerService;

public class TimerFragment extends Fragment implements View.OnClickListener{

    private MyNumberPicker npHour, npMin, npSec;
    private Button btnStart, btnStop, btnTakeTime, btnShowTimers;
    public final static String CONNECTION_TO_TIMERSERVICET = "com.example.develop.fragment_timerservice";
    private boolean timerStoped = true;
    private Intent timerIntent;
    private final static String SECONDS = "seconds", MINUTS = "minuts", HOURS = "hours", TIMER_STOPED = "timerStoped";
    private final static String SECONDS_DB = "second", MINUTS_DB = "minut", HOURS_DB = "hour", DB_NAME = "mytableTimer";
    private SharedPreferencesHelper prefs;
    private BroadcastReceiver br;
    private int seconds = 0, minuts = 0, hours = 0;
    private String strSeconds = "", strMinuts = "", strHours = "", titleMinuts, titleHours;
    private TextView tvHours, tvMinuts, tvSeconds;
    private ContentValues cv;
    private TimerDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        timerIntent = new Intent(getActivity(), TimerService.class);
        prefs = new SharedPreferencesHelper(getActivity());
        initDB();
        initViews(v);
        initValues();
        initReciever();
        IntentFilter intFilt = new IntentFilter(CONNECTION_TO_TIMERSERVICET);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, intFilt);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!prefs.getSaveTimer()) {
            int clearCount = db.delete(DB_NAME, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTimer:
                startButton();
                break;
            case R.id.btnStopTimer:
                stopButton();
                break;
            case R.id.btnTakeTimer:
                takeTimeButton();
                break;
            case R.id.btnShowTimers:
                showTimersButton();
                break;
        }
    }

    public interface OnFragmentTimerStartListener { // интерфейс для передачи активности сообщения об остановке таймера
        public void onFragmentTimerStart();
        public void showSavedTimers();
    }

    private void startButton() {
        if (timerStoped) {
            OnFragmentTimerStartListener listener = (OnFragmentTimerStartListener) getActivity();
            listener.onFragmentTimerStart();
            timerStoped = false;
            btnStart.setPressed(true);
            btnStop.setPressed(false);
            prefs.setTimerStopedFALSE(TIMER_STOPED);
            timerIntent.putExtra(HOURS, npHour.getValue());
            timerIntent.putExtra(MINUTS, npMin.getValue());
            timerIntent.putExtra(SECONDS, npSec.getValue());
            getActivity().startService(timerIntent);
        }
    }

    public void stopButton() {                   //нужно вызвать из активности
        getActivity().stopService(timerIntent);
        timerStoped = true;
        btnStart.setPressed(false);
        btnStop.setPressed(true);
        prefs.setTimerStopedTRUE(TIMER_STOPED);
        if (!prefs.getSaveTimer()) {
            int clearCount = db.delete(DB_NAME, null, null);
        }
    }

    private void takeTimeButton() {
        if ((!timerStoped) && (hours != 0 || minuts != 0 || seconds != 0)) {
            String sh = String.valueOf(hours);
            String sm = String.valueOf(minuts);
            String ss = String.valueOf(seconds);
            cv.put(HOURS_DB, sh);
            cv.put(MINUTS_DB, sm);
            cv.put(SECONDS_DB, ss);
            long rowID = db.insert(DB_NAME, null, cv);
        }
    }

    private void showTimersButton() {
        OnFragmentTimerStartListener listener = (OnFragmentTimerStartListener) getActivity();
        listener.showSavedTimers();
    }

    private void initViews(View v) {
        tvHours = (TextView) v.findViewById(R.id.textviewHoursFRT);
        tvMinuts = (TextView) v.findViewById(R.id.textviewMinutsFRT);
        tvSeconds = (TextView) v.findViewById(R.id.textviewSecondsFRT);
        npHour = (MyNumberPicker) v.findViewById(R.id.numberPickerHour);
        npMin = (MyNumberPicker) v.findViewById(R.id.numberPickerMin);
        npSec = (MyNumberPicker) v.findViewById(R.id.numberPickerSec);
        btnStart = (Button) v.findViewById(R.id.btnStartTimer);
        btnStop = (Button) v.findViewById(R.id.btnStopTimer);
        btnTakeTime = (Button) v.findViewById(R.id.btnTakeTimer);
        btnShowTimers = (Button) v.findViewById(R.id.btnShowTimers);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnTakeTime.setOnClickListener(this);
        btnShowTimers.setOnClickListener(this);
        initPressedButton();
    }

    private void initValues() {
        titleHours = getString(R.string.hoursStr);
        titleMinuts = getString(R.string.minutsStr);
    }

    private void initDB() {
        cv = new ContentValues();
        dbHelper = new TimerDatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        c = db.query(DB_NAME, null, null, null, null, null, null);
    }

    private void initReciever() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // действия при получении данных от сервиса
                timerStoped = false;
                btnStart.setPressed(true);
                btnStop.setPressed(false);
                seconds = intent.getIntExtra(SECONDS, 0);
                minuts = intent.getIntExtra(MINUTS, 0);
                hours = intent.getIntExtra(HOURS, 0);
                if (hours == 0 && minuts == 0 && seconds == 0) {
                    prefs.setTimerStopedTRUE(TIMER_STOPED);
                    timerStoped = true;
                }
                strMinuts = Integer.toString(minuts);
                strSeconds = Integer.toString(seconds);
                strHours = Integer.toString(hours);
                tvHours.setText(titleHours + strHours);
                tvMinuts.setText(titleMinuts + strMinuts);
                tvSeconds.setText(strSeconds);
            }
        };
    }

    private void initPressedButton() {
        if (prefs.getTimerStoped(TIMER_STOPED)) {
            prefs.setTimerStopedTRUE(TIMER_STOPED);
            timerStoped = true;
            btnStop.setPressed(true);
            btnStart.setPressed(false);
        } else {
            timerStoped = false;
            btnStart.setPressed(true);
            btnStop.setPressed(false);
        }
    }
}
