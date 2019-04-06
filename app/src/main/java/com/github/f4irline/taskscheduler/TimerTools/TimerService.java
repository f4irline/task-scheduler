package com.github.f4irline.taskscheduler.TimerTools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimerService extends Service implements Runnable {
    public static boolean isRunning;
    private Thread thread;
    private int seconds;
    private int minutes;
    private int hours;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("TimerService", "onDestroy");
        editor = sharedPref.edit();
        editor.putInt("seconds", 0);
        editor.putInt("minutes", 0);
        editor.putInt("hours", 0);
        editor.apply();
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        isRunning = false;
        sharedPref = getSharedPreferences("Time", Context.MODE_PRIVATE);
        seconds = seconds + sharedPref.getInt("seconds", 0);
        minutes = minutes + sharedPref.getInt("minutes", 0);
        hours = hours + sharedPref.getInt("hours", 0);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Log.d("TimerService", "Tick");
                Intent intent = new Intent("android.intent.action.TIME_TICK");
                intent.putExtra("seconds", seconds);
                intent.putExtra("minutes", minutes);
                intent.putExtra("hours", hours);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                calculateTime();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void calculateTime() {
        seconds++;
        if (seconds % 60 == 0) {
            minutes++;
            seconds = 0;
            if (minutes % 60 == 0) {
                hours++;
                minutes = 0;
            }
        }

        editor = sharedPref.edit();
        editor.putInt("seconds", seconds);
        editor.putInt("minutes", minutes);
        editor.putInt("hours", hours);
        editor.apply();
    }
}
