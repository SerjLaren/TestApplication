package com.example.testapplication;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StopwatchActivity extends AppCompatActivity implements View.OnClickListener {

    private int second = 0, minut = 0, actionbarItem = 0;
    private TextView tvSeconds, tvMinuts;
    private String strMinut = "", strSecond = "", titleMinuts = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);
        titleMinuts = getString(R.string.minutsStr);
        tvSeconds = (TextView) findViewById(R.id.textviewSeconds);
        tvMinuts = (TextView) findViewById(R.id.textviewMinuts);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                StopwatchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (actionbarItem == 1) {
                            second = 0;
                            minut = 0;
                            actionbarItem = 0;
                        }
                        if (second >= 60) {
                            minut++;
                            second = 0;
                        }
                        strMinut = Integer.toString(minut);
                        strSecond = Integer.toString(second++);
                        tvMinuts.setText(titleMinuts + strMinut);
                        tvSeconds.setText(strSecond);
                    }
                });
            }
        }, 0, 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stopwatch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.secondsRestart) {
            actionbarItem = 1;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", second);
        outState.putInt("minuts", minut);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        second = savedInstanceState.getInt("seconds");
        minut = savedInstanceState.getInt("minuts");
    }

    @Override
    public void onClick(View v) {

    }
}
