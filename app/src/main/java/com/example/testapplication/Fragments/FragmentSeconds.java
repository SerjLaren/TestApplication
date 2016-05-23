package com.example.testapplication.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.testapplication.Classes.TimerService;
import com.example.testapplication.R;

public class FragmentSeconds extends Fragment {

    private int second, minut;
    public boolean timerStoped = true;
    private String strMinut = "", strSecond = "", titleMinuts = "";
    TextView tvSeconds, tvMinuts;
    private BroadcastReceiver br;
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;
    private final static String SECONDS = "seconds", MINUTS = "minuts", TIMER_STOPED = "timerStoped";
    public final static String CONNECTION_TO_TIMERSERVICE = "com.example.develop.fragment_timerservice";

    final String LOG_TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentseconds, container, false);
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        edit = sPref.edit();
        if (((sPref.getBoolean(TIMER_STOPED, true)) == (true))) {
            edit.putBoolean(TIMER_STOPED, true);
            edit.commit();
            timerStoped = true;
        } else
        timerStoped = false;

        final Button buttonStart = (Button) v.findViewById(R.id.btnStart);
        buttonStart.setOnClickListener(new OnClickListener() {
            public void onClick (View v){
                if(timerStoped) {
                    getActivity().startService(new Intent(getActivity(), TimerService.class));
                    edit.putBoolean(TIMER_STOPED, false);
                    edit.commit();
                    timerStoped = false;
                }
            }
        });

        Button buttonStop = (Button) v.findViewById(R.id.btnStop);
        buttonStop.setOnClickListener(new OnClickListener() {
            public void onClick (View v){
                timerStoped = true;
                edit.putBoolean(TIMER_STOPED, true);
                edit.commit();
                getActivity().stopService(new Intent(getActivity(), TimerService.class));
            }
        });

        tvSeconds = (TextView) v.findViewById(R.id.textviewSecondsFR);
        tvMinuts = (TextView) v.findViewById(R.id.textviewMinutsFR);
        tvMinuts.setText("");
        tvSeconds.setText("");
        titleMinuts = getString(R.string.minutsStr);
        strMinut = Integer.toString(minut);
        strSecond = Integer.toString(second);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // действия при получении данных от сервиса
                timerStoped = false;
                second = intent.getIntExtra(SECONDS, 0);
                minut = intent.getIntExtra(MINUTS, 0);
                strMinut = Integer.toString(minut);
                strSecond = Integer.toString(second);
                tvMinuts.setText(titleMinuts + strMinut);
                tvSeconds.setText(strSecond);
            }
        };
        IntentFilter intFilt = new IntentFilter(CONNECTION_TO_TIMERSERVICE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, intFilt);
        return v;
    }
}
