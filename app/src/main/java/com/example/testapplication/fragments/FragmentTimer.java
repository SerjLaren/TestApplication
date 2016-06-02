package com.example.testapplication.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.services.TimerService;

public class FragmentTimer extends Fragment implements View.OnClickListener{

    private NumberPicker npHour, npMin, npSec;
    private Button btnStart, btnStop;
    public final static String CONNECTION_TO_TIMERSERVICET = "com.example.develop.fragment_timerservice";
    private boolean timerStoped = true;
    private Intent timerIntent;
    private String SECONDS, MINUTS, HOURS, TIMER_STOPED = "timerStoped";
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;
    private BroadcastReceiver br;
    private int seconds = 0, minuts = 0, hours = 0;
    private String strSeconds = "", strMinuts = "", strHours = "", titleMinuts, titleHours;
    private TextView tvHours, tvMinuts, tvSeconds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SECONDS = "seconds";
        MINUTS = "minuts";
        HOURS = "hours";
        titleHours = getString(R.string.hoursStr);
        titleMinuts = getString(R.string.minutsStr);
        tvHours = (TextView) v.findViewById(R.id.textviewHoursFRT);
        tvMinuts = (TextView) v.findViewById(R.id.textviewMinutsFRT);
        tvSeconds = (TextView) v.findViewById(R.id.textviewSecondsFRT);
        npHour = (NumberPicker) v.findViewById(R.id.numberPickerHour);
        npMin = (NumberPicker) v.findViewById(R.id.numberPickerMin);
        npSec = (NumberPicker) v.findViewById(R.id.numberPickerSec);
        btnStart = (Button) v.findViewById(R.id.btnStartTimer);
        btnStop = (Button) v.findViewById(R.id.btnStopTimer);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npMin.setMinValue(0);
        npMin.setMaxValue(59);
        npSec.setMinValue(0);
        npSec.setMaxValue(59);
        timerIntent = new Intent(getActivity(), TimerService.class);
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        edit = sPref.edit();
        if ((sPref.getBoolean(TIMER_STOPED, true))) {
            edit.putBoolean(TIMER_STOPED, true);
            edit.commit();
            timerStoped = true;
        } else
            timerStoped = false;

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
                    edit.putBoolean(TIMER_STOPED, true);
                    edit.commit();
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
        IntentFilter intFilt = new IntentFilter(CONNECTION_TO_TIMERSERVICET);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, intFilt);

        return v;
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
            default:
                break;
        }
    }

    private void startButton() {
        if (timerStoped) {
            timerStoped = false;
            edit.putBoolean(TIMER_STOPED, false);
            edit.commit();
            timerIntent.putExtra(HOURS, npHour.getValue());
            timerIntent.putExtra(MINUTS, npMin.getValue());
            timerIntent.putExtra(SECONDS, npSec.getValue());
            getActivity().startService(timerIntent);
        }
    }

    public void stopButton() {                   //нужно вызвать из активности
        getActivity().stopService(timerIntent);
        timerStoped = true;
        edit.putBoolean(TIMER_STOPED, true);
        edit.commit();
    }
}
