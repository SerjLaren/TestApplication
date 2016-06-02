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
    private String[] myDataset;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList data;


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

        myDataset = getDataSet();
        doList();
        myRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getActivity(), data);
        myRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
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

    public interface OnFragmentInteractionListener { // интерфейс для передачи активности сообщения об остановке таймера

        public void onFragmentInteraction(String link);
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
                if (!timerStoped) {
                    String sm = String.valueOf(minut);
                    String ss = String.valueOf(second);
                    cv.put("minut", sm);
                    cv.put("second", ss);
                    long rowID = db.insert("mytable", null, cv);
                    c = db.query("mytable", null, null, null, null, null, null);
                    doList();
                    mAdapter.notifyItemInserted(data.size()); // не вставляет элемент
                    mAdapter.notifyDataSetChanged();  // не обновляет
                    //myRecyclerView.notifyAll();
                    //mAdapter.notifyDataSetChanged();  // вставку элемента переделать, разобраться с recyclerview лучше
                    //myDataset = getDataSet();
                    //mAdapter.notifyDataSetChanged();
                    /*mAdapter = new RecyclerAdapter(myDataset);
                    myRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyItemInserted(c.getCount());*/
                }
                /*c.requery();
                c = db.query("mytable", null, null, null, null, null, null);
                myDataset[c.getCount()] += sm + " " + ss;*/
                //myDataset = getDataSet();
                //mAdapter.notifyDataSetChanged();
                //myRecyclerView.invalidateItemDecorations();
                //myRecyclerView.setLayoutManager(mLayoutManager);
                //mAdapter = new RecyclerAdapter(myDataset);
                //myRecyclerView.setAdapter(mAdapter);
                //mAdapter.notifyDataSetChanged();
                //myRecyclerView.getAdapter().notifyDataSetChanged();
                //mAdapter.notifyItemInserted(c.getCount());
                /*c = db.query("mytable", null, null, null, null, null, null);
                scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.fragment_seconds_lv_item, c, from, to);
                lvSeconds.setAdapter(scAdapter);*/

                break;
            default:
                break;
        }
    }

    private String[] getDataSet() {
        String[] mDataSet = new String[c.getCount()];
        int i = 0;
        if (c.moveToFirst())
        do {
            mDataSet[i] = c.getString(c.getColumnIndex("minut")) + " " + c.getString(c.getColumnIndex("second"));
            i++;
        }
        while (c.moveToNext());
        return mDataSet;
    }

    private void doList() {
        data = new ArrayList();
        if (c.moveToFirst())
            do {
                data.add(c.getString(c.getColumnIndex("second")));
            }
            while (c.moveToNext());
    }

    public void startButton() {
        if (timerStoped) {
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
}
