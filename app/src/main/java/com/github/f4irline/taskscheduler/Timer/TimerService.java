package com.github.f4irline.taskscheduler.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimerService extends Service implements Runnable {
    public static boolean isRunning;
    private Thread thread;
    private int seconds;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            thread.start();
            seconds = 0;
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        isRunning = false;
        thread = new Thread(this);
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Log.d("TimerService", "Tick");
                Intent intent = new Intent("android.intent.action.TIME_TICK");
                intent.putExtra("time", seconds);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                seconds++;
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
