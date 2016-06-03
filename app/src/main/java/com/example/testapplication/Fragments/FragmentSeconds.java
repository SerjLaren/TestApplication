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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapplication.helpers.DatabaseHelper;
import com.example.testapplication.adapters.RecyclerAdapter;
import com.example.testapplication.services.SecondsService;
import com.example.testapplication.R;

import java.util.ArrayList;

public class FragmentSeconds extends Fragment implements View.OnClickListener{

    private int second, minut;
    public boolean timerStoped = true;
    private String strMinut = "", strSecond = "", titleMinuts = "";
    private TextView tvSeconds, tvMinuts;
    private BroadcastReceiver br;
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;
    private final static String SECONDS = "seconds", MINUTS = "minuts", TIMER_STOPED = "secondsStoped";
    public final static String CONNECTION_TO_TIMERSERVICE = "com.example.develop.fragment_secondsservice";
    final String LOG_TAG = "myLogs";
    private Button buttonStart, buttonStop, buttonGet;
    private ContentValues cv;
    private DatabaseHelper dbHelper;
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
        View v = inflater.inflate(R.layout.fragment_seconds, container, false);
        cv = new ContentValues();
        dbHelper = new DatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        c = db.query("mytable", null, null, null, null, null, null);
        doList();
        myRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getActivity(), data);
        myRecyclerView.setAdapter(mAdapter);
        myRecyclerView.setHasFixedSize(true);
        itemAnimator = new DefaultItemAnimator();
        myRecyclerView.setItemAnimator(itemAnimator);

        buttonStart = (Button) v.findViewById(R.id.btnStart);
        buttonStop = (Button) v.findViewById(R.id.btnStop);
        buttonGet = (Button) v.findViewById(R.id.btnGet);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonGet.setOnClickListener(this);
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        edit = sPref.edit();
        if ((sPref.getBoolean(TIMER_STOPED, true))) {
            edit.putBoolean(TIMER_STOPED, true);
            edit.commit();
            timerStoped = true;
        } else
        timerStoped = false;

        if (timerStoped) {
            buttonStart.setPressed(false);
            buttonStop.setPressed(true);
        } else {
            buttonStart.setPressed(true);
            buttonStop.setPressed(false);
        }

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
        IntentFilter intFilt = new IntentFilter(CONNECTION_TO_TIMERSERVICE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, intFilt);
        return v;
    }

    public interface OnFragmentSecStartListener { // интерфейс для передачи активности сообщения об остановке таймера
        public void onFragmentSecStart();
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
            case R.id.btnGet:
                getButton();
                break;
            default:
                break;
        }
    }

    private void doList() {
        data = new ArrayList();
        if (c.moveToFirst())
            do {
                data.add(c.getString(c.getColumnIndex("minut")) + " " + c.getString(c.getColumnIndex("second")));
            }
            while (c.moveToNext());
    }

    private void startButton() {
        if (timerStoped) {
            OnFragmentSecStartListener listener = (OnFragmentSecStartListener) getActivity();
            listener.onFragmentSecStart();
            getActivity().startService(new Intent(getActivity(), SecondsService.class));
            edit.putBoolean(TIMER_STOPED, false);
            edit.commit();
            timerStoped = false;
        }
    }

    public void stopButton() {
        timerStoped = true;
        edit.putBoolean(TIMER_STOPED, true);
        edit.commit();
        getActivity().stopService(new Intent(getActivity(), SecondsService.class));
        int clearCount = db.delete("mytable", null, null);
        data.clear();
        mAdapter.notifyItemRangeRemoved(0, c.getCount());
    }

    private void getButton() {
        if (!timerStoped) {
            String sm = String.valueOf(minut);
            String ss = String.valueOf(second);
            cv.put("minut", sm);
            cv.put("second", ss);
            long rowID = db.insert("mytable", null, cv);
            db = dbHelper.getReadableDatabase();
            c = db.query("mytable", null, null, null, null, null, null);
            c.moveToLast();
            data.add(c.getString(c.getColumnIndex("minut")) + " " + c.getString(c.getColumnIndex("second")));
            mAdapter.notifyItemInserted(data.size()); // не вставляет элемент
        }
    }
}
