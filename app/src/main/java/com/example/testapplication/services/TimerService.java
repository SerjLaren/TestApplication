package com.example.testapplication.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.testapplication.R;
import com.example.testapplication.activities.StopwatchActivity;
import com.example.testapplication.fragments.TimerFragment;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private Intent intentTimer = new Intent(TimerFragment.CONNECTION_TO_TIMERSERVICET);
    private Timer myTimer;
    private TimerTask myTimerTask;
    private int seconds = 0, minuts = 0, hours = 0;
    private String notifTitle, titleHourNotif, titleMinNotif, titleSecNotif, notifTitleDone, titleDoneNotif, titleDisplayDone;
    private Notification.Builder builder;
    private Notification notification;
    private NotificationManagerCompat notificationManager;
    private int notifID = 25, notifDoneID = 26;
    private final static String SECONDS = "seconds", MINUTS = "minuts", HOURS = "hours";
    boolean timerDone = true;

    public void onCreate() {
        super.onCreate();
        notifTitle = getString(R.string.notifTitleTimer);
        titleHourNotif = getString(R.string.titleHourNotif);
        titleMinNotif = getString(R.string.titleMinNotif);
        titleSecNotif = getString(R.string.titleSecNotif);
        titleDisplayDone = getString(R.string.titleDisplayDone);
        notifTitleDone = getString(R.string.notifTitleTimerDone);
        titleDoneNotif = getString(R.string.timerDone);
        myTimer = new Timer();
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (seconds <= 0) {
                    if (minuts <= 0) {
                        if (hours <= 0) {
                            timerDone = true;
                        } else {
                            timerDone = false;
                            hours--;
                            minuts = 59;
                            seconds = 59;
                        }
                    } else {
                        timerDone = false;
                        minuts--;
                        seconds = 59;
                    }
                } else {
                    timerDone = false;
                    seconds--;
                }
                intentTimer.putExtra(HOURS, hours);
                intentTimer.putExtra(MINUTS, minuts);
                intentTimer.putExtra(SECONDS, seconds);
                sendData();
                sendNotif(); // уведомление в "шторку"
                if (timerDone) {
                    myTimer.cancel();
                    notificationManager.cancel(notifID);
                    sendNotifDone();
                    stopSelf();
                }
            }
        };
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            hours = intent.getIntExtra(HOURS, 0);
            minuts = intent.getIntExtra(MINUTS, 0);
            seconds = intent.getIntExtra(SECONDS, 0) + 1;
        }
        myTimer.schedule(myTimerTask, 0, 1000);
        return START_STICKY;
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
                .setContentText(titleHourNotif + String.valueOf(hours) + titleMinNotif + String.valueOf(minuts) + titleSecNotif + String.valueOf(seconds)); // Текст уведомления
        notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notifID, notification);
    }

    void sendNotifDone() {
        Intent stopwatchActIntent = new Intent(this, StopwatchActivity.class);
        stopwatchActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, stopwatchActIntent, 0);
        notificationManager.cancel(notifDoneID);
        builder = new Notification.Builder(this);
        builder.setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setTicker(titleDisplayDone)
                .setContentTitle(notifTitleDone)
                .setContentText(titleDoneNotif); // Текст уведомления
        notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notifDoneID, notification);
    }

    public void sendData () {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentTimer);
    }
}

