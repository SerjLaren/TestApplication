package com.example.testapplication.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.testapplication.activities.StopwatchActivity;
import com.example.testapplication.fragments.FragmentSeconds;
import com.example.testapplication.R;

import java.util.Timer;
import java.util.TimerTask;

public class SecondsService extends Service {

    private Intent intentSeconds = new Intent(FragmentSeconds.CONNECTION_TO_TIMERSERVICE);
    private Timer myTimer;
    private TimerTask myTimerTask;
    private int seconds = 0, minuts = 0;
    private String notifTitle, titleMinNotif, titleSecNotif;
    private Notification.Builder builder;
    private Notification notification;
    private NotificationManagerCompat notificationManager;
    private int notifID = 24;
    private String SECONDS, MINUTS;


    public void onCreate() {
        super.onCreate();
        SECONDS = "seconds";
        MINUTS = "minuts";
        notifTitle = getString(R.string.notifTitle);
        titleMinNotif = getString(R.string.titleMinNotif);
        titleSecNotif = getString(R.string.titleSecNotif);
        myTimer = new Timer();
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                seconds++;
                if (seconds >= 60) {
                    minuts++;
                    seconds = 0;
                }
                intentSeconds.putExtra(SECONDS, seconds);
                intentSeconds.putExtra(MINUTS, minuts);
                sendNotif();         // уведомление в "шторку"
                sendData();
            }
        };
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        myTimer.schedule(myTimerTask, 0, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
        notificationManager.cancel(notifID);
    }

    public IBinder onBind(Intent arg0) {
        return  null;
    }

    void sendNotif() {
        Intent stopwatchActIntent = new Intent(this, StopwatchActivity.class);
        stopwatchActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, stopwatchActIntent, 0);
        builder = new Notification.Builder(this);
        builder.setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notifTitle)
                .setContentText(titleMinNotif + String.valueOf(minuts) + titleSecNotif + String.valueOf(seconds)); // Текст уведомления
        notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notifID, notification);
    }

    public void sendData () {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentSeconds);
    }

}
